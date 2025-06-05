package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    @Test
    void testExistsByEmail() {
        User user = new User(null, "John", "john@example.com");
        entityManager.persistAndFlush(user);

        boolean exists = userRepo.existsByEmail("john@example.com");

        assertThat(exists).isTrue();
    }
}
