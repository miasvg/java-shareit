package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepo {

     Booking save(Booking booking);

     Optional<Booking> findById(Long id);

     List<Booking> findAllByBookerId(Long bookerId);

     List<Booking> findAllByItemOwnerId(Long ownerId);

}
