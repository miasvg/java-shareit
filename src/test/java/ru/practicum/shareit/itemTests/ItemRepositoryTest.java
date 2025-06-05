package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;


@DataJpaTest
@Sql(scripts = "/test-data.sql")
public class ItemRepositoryTest {

    @Autowired
    private ItemRepo itemRepository;

    @Test
    void findAllByOwnerId_ShouldReturnItems() {
        List<Item> items = itemRepository.findAllByOwnerId(1L);
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("Дрель");

    }

    @Test
    void search_ShouldFindAvailableItems() {
        List<Item> items = itemRepository.search("дрель");
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Дрель");

    }
}