package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemServiceInt;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceInt;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements BookingServiceInt {
    private final BookingRepo bookingRepository;
    private final UserServiceInt userService;
    private final ItemServiceInt itemService;

    @Override
    public BookingResponseDto createBooking(BookingDto bookingDto, Long bookerId) {
        User booker = userService.getUserEntityById(bookerId);
        Item item = itemService.getItemEntityById(bookingDto.getItemId());
        validateBookingCreation(bookingDto, bookerId, item);
        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Подтверждать бронирование может только владелец вещи");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование уже было подтверждено или отклонено");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        // Проверяем, что запрашивает владелец или автор бронирования
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Доступ запрещён");
        }

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(Long bookerId, String state) {
        userService.getUserById(bookerId);
        List<Booking> bookings = bookingRepository.findAllByBookerId(bookerId);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long ownerId, String state) {
        userService.getUserById(ownerId);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerId(ownerId);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    private List<Booking> filterBookingsByState(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "ALL":
                return bookings;
            case "CURRENT":
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .toList();
            case "PAST":
                return bookings.stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .toList();
            case "FUTURE":
                return bookings.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .toList();
            case "WAITING":
                return bookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.WAITING)
                        .toList();
            case "REJECTED":
                return bookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.REJECTED)
                        .toList();
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }
    private void validateBookingCreation(BookingDto bookingDto, Long bookerId, Item item) {
        if (!item.getAvailable()) {
            throw new UnavailableItemException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("Нельзя бронировать свою вещь");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания должна быть после даты начала");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата начала должна быть в будущем");
        }
    }
}
