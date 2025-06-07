package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDto dto) {
        log.info("Create booking: {}", dto);
        return bookingService.createBooking(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(@PathVariable Long bookingId,
                                                  @RequestParam boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) throws AccessDeniedException {
        log.info("Update booking: {}", bookingId);
        return bookingService.updateBookingStatus(ownerId,bookingId,approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get booking: {}", bookingId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsForUser(@RequestParam(defaultValue = "ALL") String state,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get bookings for user: {}", userId);
        return bookingService.getBookingsForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForOwner(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Get bookings for owner: {}", ownerId);
        return bookingService.getBookingsForOwner(ownerId, state);
    }
}