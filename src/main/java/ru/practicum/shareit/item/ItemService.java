// ItemService.java
package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserServiceInt;
import ru.practicum.shareit.booking.BookingRepo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ItemService implements ItemServiceInt {
    private final ItemRepo itemRepository;
    private final UserServiceInt userService;
    private final UserMapper userMapper;
    private final BookingRepo bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepo itemRepository, UserService userService, BookingRepo bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.userMapper =  new UserMapper();
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemMapper = new ItemMapper();
    }

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
     // Проверяем существование пользователя
        User owner = userMapper.toUser(userService.getUserById(ownerId));
        itemDto.setOwnerId(ownerId); // Прямо перед маппингом
        Item item = ItemMapper.toItem(itemDto, owner);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Нельзя редактировать чужую вещь");
        }

        if (itemDto.getName() != null) existingItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null) existingItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) existingItem.setAvailable(itemDto.getAvailable());

        Item updatedItem = itemRepository.save(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        ItemDto dto = ItemMapper.toItemDto(item);
        List<CommentDto> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId)
                .stream().map(CommentMapper::toDto).toList();
        dto.setComments(comments);
        return ItemMapper.toItemDto(item);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList(); // <--- правильное поведение
        }

        return itemRepository.search(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

    }


    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, String text) {
        if (text == null || text.isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым");
        }

        User author = userMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean hasCompletedBooking = bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(
                userId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);

        if (!hasCompletedBooking) {
            throw new ValidationException("Оставлять отзыв можно только после завершённого бронирования");
        }

        Comment comment = CommentMapper.toEntity(text, author, item);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, List<CommentDto>> commentsMap = commentRepository.findByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(
                        c -> c.getItem().getId(),
                        Collectors.mapping(CommentMapper::toDto, Collectors.toList())
                ));

        return items.stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    dto.setComments(commentsMap.getOrDefault(item.getId(), List.of()));
                    return dto;
                })
                .toList();
    }


}