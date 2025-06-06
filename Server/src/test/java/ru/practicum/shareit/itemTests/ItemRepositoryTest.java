package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ItemMapper.class})
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepo itemRepo;

    @Test
    void findAllByOwnerId_shouldReturnCorrectItems() {
        Item item1 = new Item(null, "item1", "desc1", true, 1L, null);
        Item item2 = new Item(null, "item2", "desc2", true, 2L, null);
        em.persist(item1);
        em.persist(item2);

        List<Item> found = itemRepo.findAllByOwnerId(1L);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("item1");
    }

    @Test
    void search_shouldReturnMatchingItems() {
        em.persist(new Item(null, "Drill", "Power tool", true, 1L, null));
        em.persist(new Item(null, "Hammer", "Strong tool", true, 1L, null));
        em.persist(new Item(null, "Screwdriver", "Precision", false, 1L, null));

        List<Item> result = itemRepo.search("tool");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Item::getName).contains("Drill", "Hammer");
    }
}

