package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {

     // Поиск по владельцу с пагинацией
     @Query("SELECT i FROM Item i WHERE i.ownerId = :ownerId ORDER BY i.id")
     Page<Item> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

     @Query("SELECT i FROM Item i WHERE i.ownerId = :ownerId")
     List<Item> findAllByOwnerId(@Param("ownerId") Long ownerId);

     // Поиск доступных вещей по тексту
     @Query("SELECT i FROM Item i WHERE " +
             "(LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%) " +
             "AND i.available = true")
     List<Item> search(@Param("text") String text);




     // Поиск по запросу
     List<Item> findByRequestId(Long requestId);

     // Проверка существования вещи у пользователя
     boolean existsByIdAndOwnerId(Long itemId, Long ownerId);
}
