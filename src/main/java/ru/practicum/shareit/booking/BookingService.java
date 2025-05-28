package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceInt;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.User;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService implements BookingServiceInt {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepo bookingRepository;
    private final UserServiceInt userService;
    private final ItemRepo itemRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(Long bookerId, BookingDto bookingDto) {
        UserMapper userMapper = new UserMapper();
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания не может быть раньше даты начала.");
        }

        User booker = userMapper.toUser(userService.getUserById(bookerId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования.");
        }

        if (item.getOwnerId().equals(bookerId)) {
            throw new NotFoundException("Владелец не может бронировать свою вещь.");
        }

        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return toResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long ownerId, Long bookingId, boolean approved) throws AccessDeniedException {
        log.info("Получен запрос на подтверждение бронирования ID: {} пользователем ID: {}", bookingId, ownerId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        log.info("Бронирование найдено: {}", booking);


        if (!booking.getItem().getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("Подтвердить бронирование может только владелец.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Статус уже установлен.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return toResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Нет доступа к бронированию.");
        }

        return toResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsForUser(Long userId, String state, Pageable pageable) {
        userService.getUserById(userId); // проверка существования
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (BookingState.valueOf(state.toUpperCase())) {
            case CURRENT -> bookings = bookingRepository.findCurrentForUser(userId, now, pageable);
            case PAST -> bookings = bookingRepository.findPastForUser(userId, now, pageable);
            case FUTURE -> bookings = bookingRepository.findFutureForUser(userId, now, pageable);
            case WAITING -> bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable);
            case REJECTED -> bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable);
            case ALL -> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId, pageable);
            default -> throw new ValidationException("Неверный параметр state: " + state);
        }

        return bookings.stream().map(this::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsForOwner(Long ownerId, String state, Pageable pageable) {
        userService.getUserById(ownerId); // проверка существования
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (BookingState.valueOf(state.toUpperCase())) {
            case CURRENT -> bookings = bookingRepository.findCurrentForOwner(ownerId, now, pageable);
            case PAST -> bookings = bookingRepository.findPastForOwner(ownerId, now, pageable);
            case FUTURE -> bookings = bookingRepository.findFutureForOwner(ownerId, now, pageable);
            case WAITING -> bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING, pageable);
            case REJECTED -> bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED, pageable);
            case ALL -> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId, pageable);
            default -> throw new ValidationException("Неверный параметр state: " + state);
        }

        return bookings.stream().map(this::toResponseDto).toList();
    }

    private BookingResponseDto toResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                new UserDto(booking.getBooker().getId(), booking.getBooker().getName(), booking.getBooker().getEmail()),
                booking.getStatus()
        );
    }
}



