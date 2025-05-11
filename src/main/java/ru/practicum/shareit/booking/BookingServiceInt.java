package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingServiceInt {

     BookingResponseDto createBooking(BookingDto bookingDto, Long bookerId);

     BookingResponseDto approveBooking(Long bookingId, Long ownerId, boolean approved);

     BookingResponseDto getBookingById(Long bookingId, Long userId);

     List<BookingResponseDto> getUserBookings(Long bookerId, String state);

     List<BookingResponseDto> getOwnerBookings(Long ownerId, String state);

}
