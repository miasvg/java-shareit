package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {

     List<Booking> findByBookerIdOrderByStartDesc(Long userId);

     List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);


     Optional<Booking> findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(
             Long bookerId, Long itemId, LocalDateTime now);

     List<Booking> findByStatusAndItem_id(BookingStatus status, Long itemId);


     @Query("SELECT b FROM Booking b JOIN FETCH b.item i JOIN FETCH b.booker WHERE b.id = :id")
     Optional<Booking> findByIdWithItemAndBooker(@Param("id") Long id);

}
