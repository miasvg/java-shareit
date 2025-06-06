package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepo userRepo;

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        User user = new User(null, "John", "john@example.com");
        em.persist(user);

        boolean exists = userRepo.existsByEmail("john@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailNotExists() {
        boolean exists = userRepo.existsByEmail("nope@example.com");

        assertFalse(exists);
    }

    @Test
    void findById_ShouldReturnUser() {
        User user = new User(null, "John", "john@example.com");
        em.persist(user);

        Optional<User> found = userRepo.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getName());
    }
}
