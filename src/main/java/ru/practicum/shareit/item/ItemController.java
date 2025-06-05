// ItemController.java
package ru.practicum.shareit.item;

import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceInt itemService;
    private final static Logger log = LoggerFactory.getLogger(ItemService.class);

    @PostMapping
    public ItemResponseDto createItem(
            @RequestBody @Valid ItemCreateDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Creating new item: {}", itemDto);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
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
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Getting items by owner: {}", ownerId);
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Searching for items with text {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment( @RequestHeader ("X-Sharer-User-Id") long userId,
                                  @PathVariable long itemId,
                                  @RequestBody CommentDto dto){
        log.info("Adding comment to item: {}", dto);
        return itemService.addComment(userId, itemId, dto);
    }
}
