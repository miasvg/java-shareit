package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepo itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepo bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepo userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_ShouldReturnItemResponseDto() {
        ItemCreateDto dto = new ItemCreateDto("Дрель", "Аккумуляторная", true, null);
        User owner = new User(1L, "Owner", "owner@mail.com");

        when(userService.getUserById(1L)).thenReturn(new UserDto(1L, "Owner", "owner@mail.com"));
        when(itemRepository.save(any())).thenAnswer(inv -> {
            Item item = inv.getArgument(0);
            item.setId(1L);
            return item;
        });

        ItemResponseDto result = itemService.createItem(dto, 1L);

        assertThat(result).extracting(ItemResponseDto::getName).isEqualTo("Дрель");
        verify(itemRepository).save(any());
    }

    @Test
    void updateItem_ShouldUpdateFields() {
        Item existing = new Item(1L, "Дрель", "Старая", true, 1L, null);
        ItemDto update = new ItemDto(null, "Дрель+", "Новая", null);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(itemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ItemDto result = itemService.updateItem(1L, update, 1L);

        assertThat(result)
                .extracting(ItemDto::getName, ItemDto::getDescription)
                .containsExactly("Дрель+", "Новая");
    }
}
