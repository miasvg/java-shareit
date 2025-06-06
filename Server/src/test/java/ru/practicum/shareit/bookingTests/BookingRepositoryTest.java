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
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepo bookingRepo;

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setup() {
        user = em.persist(new User(null, "User", "user@email.com"));
        User owner = em.persist(new User(null, "Owner", "owner@email.com"));
        item = em.persist(new Item(null, "Item", "Desc", true, owner.getId(), null));

        booking = em.persist(new Booking(null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.WAITING
        ));
    }

    @Test
    void findByBookerIdOrderByStartDesc() {
        List<Booking> found = bookingRepo.findByBookerIdOrderByStartDesc(user.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getBooker().getId()).isEqualTo(user.getId());
    }

    @Test
    void findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc() {
        booking.setEnd(LocalDateTime.now().minusHours(1));
        em.persistAndFlush(booking);

        Optional<Booking> result = bookingRepo.findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(
                user.getId(), item.getId(), LocalDateTime.now());

        assertThat(result).isPresent();
    }

    @Test
    void findByStatusAndItem_id() {
        List<Booking> found = bookingRepo.findByStatusAndItem_id(BookingStatus.WAITING, item.getId());
        assertThat(found).hasSize(1);
    }
}

