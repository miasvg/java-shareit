package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository requestRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByRequestorIdOrderByCreatedDesc() {
        // Создание пользователя
        User user = new User();
        user.setName("Оля");
        user.setEmail("olya@example.com");
        entityManager.persist(user);

        // Создание первого запроса
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Нужен ноутбук");
        request1.setRequestor(user);
        request1.setCreated(LocalDateTime.now().minusHours(2));
        entityManager.persist(request1);

        // Создание второго запроса
        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Нужен пылесос");
        request2.setRequestor(user);
        request2.setCreated(LocalDateTime.now().minusHours(1));
        entityManager.persist(request2);

        entityManager.flush();

        // Выполнение запроса
        List<ItemRequest> results = requestRepository.findByRequestorIdOrderByCreatedDesc(user.getId());

        // Проверки
        assertEquals(2, results.size());
        assertEquals("Нужен пылесос", results.get(0).getDescription()); // Последний по времени
        assertEquals("Нужен ноутбук", results.get(1).getDescription());
    }
}

