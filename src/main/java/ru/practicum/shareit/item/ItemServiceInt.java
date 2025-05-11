package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemServiceInt {

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);

    Item getItemEntityById(Long itemId);

}
