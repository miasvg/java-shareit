package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.ownerId = :ownerId")
    List<Item> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT i FROM Item i WHERE " +
            "(LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%) " +
            "AND i.available = true")
    List<Item> search(@Param("text") String text);

}
