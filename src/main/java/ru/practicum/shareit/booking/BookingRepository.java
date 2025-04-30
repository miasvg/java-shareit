package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private Long idCounter = 1L;

    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(idCounter++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public List<Booking> findAllByBookerId(Long bookerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getBooker().getId().equals(bookerId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public List<Booking> findAllByItemOwnerId(Long ownerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getItem().getOwner().getId().equals(ownerId)) {
                result.add(booking);
            }
        }
        return result;
    }
}
