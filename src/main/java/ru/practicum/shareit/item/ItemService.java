// ItemService.java
package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.booking.BookingRepo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;


@Service
public class ItemService implements ItemServiceInt {
    private final ItemRepo itemRepository;
    private final UserServiceInt userService;
    private final UserMapper userMapper;
    private final BookingRepo bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final UserRepo userRepository;

    public ItemService(ItemRepo itemRepository, UserService userService, BookingRepo bookingRepository, CommentRepository commentRepository, UserRepo userRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.userRepository = userRepository;
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
        Item item = ItemMapper.toItem(itemDto, ownerId);
        item.setOwnerId(ownerId);
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
    public ItemWithBookingsAndCommentsDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<Booking> bookings = bookingRepository.findByStatusAndItem_id(BookingStatus.WAITING,itemId);
        ItemWithBookingsAndCommentsDto dto = ItemMapper.toItemWithBookingsAndCommentsDto(item, bookings, comments);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        Booking booking = bookingRepository.findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(
                        userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("у пользователя нет брони на эту вещь"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setBooking(booking);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Неверный id пользователя"));
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);

        return items.stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    return dto;
                })
                .toList();
    }
}
