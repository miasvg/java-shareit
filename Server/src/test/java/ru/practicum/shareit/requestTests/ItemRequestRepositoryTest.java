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
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository repository;

    @Test
    void findByRequestorIdOrderByCreatedDesc_shouldReturnRequests() {
        User user = new User(null, "user", "user@mail.com");
        em.persist(user);

        ItemRequest r1 = ItemRequest.builder()
                .description("Нужен велосипед")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        em.persist(r1);

        List<ItemRequest> result = repository.findByRequestorIdOrderByCreatedDesc(user.getId());
        assertThat(result).hasSize(1).extracting(ItemRequest::getDescription).contains("Нужен велосипед");
    }
}