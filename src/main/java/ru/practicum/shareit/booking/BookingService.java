package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.*;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements BookingServiceInt {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepo bookingRepository;
    private final UserServiceInt userService;
    private final ItemRepo itemRepository;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public BookingResponseDto createBooking(Long bookerId, BookingDto bookingDto) {

        User booker = userRepo.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("пользователь не существует"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания не может быть раньше даты начала.");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования.");
        }



        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        Booking saved = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(saved);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long ownerId, Long bookingId, boolean approved) throws AccessDeniedException {
        log.info("Получен запрос на подтверждение бронирования ID: {} пользователем ID: {}", bookingId, ownerId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        log.info("Бронирование найдено: {}", booking);
        Item item = booking.getItem();

        if (!item.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("Подтвердить бронирование может только владелец.");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Статус уже установлен.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Access denied");
        }

        return BookingMapper.toBookingResponseDto(booking);
    }


    @Override
    public List<BookingResponseDto> getBookingsForUser(Long userId, String stateStr) {
        userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        BookingState state = BookingState.from(stateStr);

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);

        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(booking -> {
                    switch (state) {
                        case ALL:
                            return true;
                        case CURRENT:
                            return booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                        case PAST:
                            return booking.getEnd().isBefore(now);
                        case FUTURE:
                            return booking.getStart().isAfter(now);
                        case WAITING:
                            return booking.getStatus() == BookingStatus.WAITING;
                        case REJECTED:
                            return booking.getStatus() == BookingStatus.REJECTED;
                        default:
                            return false;
                    }
                })
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsForOwner(Long ownerId, String stateStr) {
        userRepo.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")); // проверка существования
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
        BookingState state = BookingState.from(stateStr);
        return bookings.stream()
                .filter(booking -> {
                    switch (state) {
                        case ALL:
                            return true;
                        case CURRENT:
                            return booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                        case PAST:
                            return booking.getEnd().isBefore(now);
                        case FUTURE:
                            return booking.getStart().isAfter(now);
                        case WAITING:
                            return booking.getStatus() == BookingStatus.WAITING;
                        case REJECTED:
                            return booking.getStatus() == BookingStatus.REJECTED;
                        default:
                            return false;
                    }
                })
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }
}
