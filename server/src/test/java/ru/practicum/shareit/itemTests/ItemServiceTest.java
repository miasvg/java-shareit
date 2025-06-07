package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepo;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock private ItemRepo itemRepo;
    @Mock private UserService userService;
    @Mock private UserRepo userRepo;
    @Mock private BookingRepo bookingRepo;
    @Mock private CommentRepository commentRepo;
    @Mock private ItemRequestRepository itemRequestRepo;

    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        itemService = new ItemService(itemRepo, userService, bookingRepo, commentRepo, userRepo, itemRequestRepo);
    }

    @Test
    public void createItem_shouldReturnSavedItemResponseDto() {
        ItemCreateDto dto = new ItemCreateDto("item", "desc", true, null);
        User owner = new User(1L, "test", "test@email.com");
        Item item = new Item(null, "item", "desc", true, 1L, null);
        Item savedItem = new Item(1L, "item", "desc", true, 1L, null);

        when(userService.getUserById(1L)).thenReturn(new UserDto(1L, "test", "test@email.com"));
        when(itemRepo.save(any())).thenReturn(savedItem);

        ItemResponseDto result = itemService.createItem(dto, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("item");
    }

    @Test
    public void updateItem_shouldUpdateFields() {
        Item existingItem = new Item(1L, "old", "old", true, 1L, null);
        ItemDto dto = new ItemDto(null, "new", "desc", false);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepo.save(any())).thenReturn(existingItem);

        ItemDto result = itemService.updateItem(1L, dto, 1L);

        assertThat(result.getName()).isEqualTo("new");
        assertThat(result.getAvailable()).isEqualTo(false);
    }
}
