package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookingRepository implements BookingRepo {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(idCounter++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<Booking> findAllByBookerId(Long bookerId) {
        return bookings.values().stream()
                .filter(booking -> booking.getBooker() != null &&
                        booking.getBooker().getId().equals(bookerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findAllByItemOwnerId(Long ownerId) {
        return bookings.values().stream()
                .filter(booking -> booking.getItem() != null &&
                        booking.getItem().getOwner() != null &&
                        booking.getItem().getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }
}
