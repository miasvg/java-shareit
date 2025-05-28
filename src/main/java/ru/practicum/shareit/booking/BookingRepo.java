package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId ORDER BY b.start DESC")
     Page<Booking> findAllByBookerId(@Param("userId") Long userId, Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId ORDER BY b.start DESC")
     Page<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

     List<Booking> findByItemIdAndStatusOrderByStartAsc(Long itemId, BookingStatus status);

     boolean existsByBookerIdAndItemIdAndEndBefore(Long userId, Long itemId, LocalDateTime now);

     List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

     List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
     List<Booking> findCurrent(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < :now ORDER BY b.start DESC")
     List<Booking> findPast(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start > :now ORDER BY b.start DESC")
     List<Booking> findFuture(@Param("userId") Long userId, @Param("now") LocalDateTime now, Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status = :status ORDER BY b.start DESC")
     List<Booking> findByUserAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status, Pageable pageable);


     @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.start < :now AND b.end > :now ORDER BY b.start DESC")
     List<Booking> findCurrentForOwner(@Param("ownerId") Long ownerId,
                                       @Param("now") LocalDateTime now,
                                       Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.end < :now ORDER BY b.start DESC")
     List<Booking> findPastForOwner(@Param("ownerId") Long ownerId,
                                    @Param("now") LocalDateTime now,
                                    Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.start > :now ORDER BY b.start DESC")
     List<Booking> findFutureForOwner(@Param("ownerId") Long ownerId,
                                      @Param("now") LocalDateTime now,
                                      Pageable pageable);

     List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId,
                                                              BookingStatus status,
                                                              Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start < :now AND b.end > :now ORDER BY b.start DESC")
     List<Booking> findCurrentForUser(@Param("userId") Long userId,
                                      @Param("now") LocalDateTime now,
                                      Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < :now ORDER BY b.start DESC")
     List<Booking> findPastForUser(@Param("userId") Long userId,
                                   @Param("now") LocalDateTime now,
                                   Pageable pageable);

     @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start > :now ORDER BY b.start DESC")
     List<Booking> findFutureForUser(@Param("userId") Long userId,
                                     @Param("now") LocalDateTime now,
                                     Pageable pageable);


     List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId,
                                                           BookingStatus status,
                                                           Pageable pageable);
     boolean existsByBookerIdAndItemIdAndEndBeforeAndStatus(
             Long bookerId,
             Long itemId,
             LocalDateTime end,
             BookingStatus status
     );

}

