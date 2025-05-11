package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepo {

     Item save(Item item);

     Optional<Item> findById(Long id);

     List<Item> findAllByOwnerId(Long ownerId);

     List<Item> search(String text);

}
