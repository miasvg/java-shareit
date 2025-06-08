// ItemController.java
package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceInt itemService;
    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponseDto createItem(
            @RequestBody ItemCreateDto itemDto,
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Creating new item: {}", itemDto);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Updating item: {}", itemDto);
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsAndCommentsDto getItemById(@PathVariable Long itemId) {
        log.info("Getting item by id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Getting items by owner: {}", ownerId);
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Searching for items with text {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader (X_SHARER_USER_ID) long userId,
                                  @PathVariable long itemId,
                                  @RequestBody CommentDto dto) {
        log.info("Adding comment to item: {}", dto);
        return itemService.addComment(userId, itemId, dto);
    }
}

