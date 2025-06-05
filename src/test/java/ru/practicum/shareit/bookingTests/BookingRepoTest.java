package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepo;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepo bookingRepo;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = em.persist(new User(null, "owner", "owner@mail.com"));
        user = em.persist(new User(null, "user", "user@mail.com"));

        item = em.persist(new Item(null, "Item1", "Desc", true, owner.getId(), null));
        booking = em.persist(new Booking(null,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                item, user, BookingStatus.WAITING));
    }

    @Test
    void testFindByBookerIdOrderByStartDesc() {
        List<Booking> results = bookingRepo.findByBookerIdOrderByStartDesc(user.getId());
        assertThat(results).hasSize(1);
    }

    @Test
    void testFindByStatusAndItem_id() {
        List<Booking> results = bookingRepo.findByStatusAndItem_id(BookingStatus.WAITING, item.getId());
        assertThat(results).hasSize(1);
    }

    @Test
    void testFindFirstPastBooking() {
        booking.setEnd(LocalDateTime.now().minusDays(1));
        em.persistAndFlush(booking);

        Optional<Booking> found = bookingRepo
                .findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(
                        user.getId(), item.getId(), LocalDateTime.now());
        assertThat(found).isPresent();
    }
}

