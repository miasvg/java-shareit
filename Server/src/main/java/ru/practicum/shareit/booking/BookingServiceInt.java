package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BookingServiceInt {

    BookingResponseDto createBooking(Long bookerId, BookingDto bookingDto);

    BookingResponseDto updateBookingStatus(Long ownerId, Long bookingId, boolean approved) throws AccessDeniedException;

    BookingResponseDto getBooking(Long bookingId, Long userId);

    List<BookingResponseDto> getBookingsForUser(Long userId, String stateStr);

    List<BookingResponseDto> getBookingsForOwner(Long ownerId, String state);

}
