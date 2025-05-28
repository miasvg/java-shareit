package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody @Valid BookingDto dto,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(@PathVariable Long bookingId,
                                                  @RequestParam boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) throws AccessDeniedException {
        return bookingService.updateBookingStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsForUser(@RequestParam(defaultValue = "ALL") String state,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @PageableDefault(size = 10, sort = "start", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.getBookingsForUser(userId, state, pageable);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForOwner(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @PageableDefault(size = 10, sort = "start", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.getBookingsForOwner(ownerId, state, pageable);
    }
}

