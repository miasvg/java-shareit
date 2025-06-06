package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @InjectMocks
    private ItemRequestService service;

    @Mock
    private ItemRequestRepository requestRepo;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserService userService;

    @Test
    void createRequest_shouldReturnShortDto() {
        User user = new User(1L, "Test", "test@mail.com");
        ItemRequestCreateDto dto = new ItemRequestCreateDto("Описание", LocalDateTime.now());
        ItemRequest request = ItemRequestMapper.toItemRequest(dto, user);
        request.setId(10L);

        when(userService.getUserById(1L)).thenReturn(UserMapper.toUserDto(user));
        when(requestRepo.save(any())).thenReturn(request);

        RequestShortDto result = service.createRequest(1L, dto);

        assertEquals(10L, result.getId());
        assertEquals("Описание", result.getDescription());
    }

    @Test
    void getUserRequests_shouldReturnList() {
        User user = new User(1L, "Test", "test@mail.com");
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Test")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepo.findByRequestorIdOrderByCreatedDesc(1L)).thenReturn(List.of(request));

        List<ItemRequestResponseDto> result = service.getUserRequests(1L);

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getDescription());
    }

    @Test
    void getRequestById_shouldReturnDto() {
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Описание")
                .created(LocalDateTime.now())
                .requestor(new User())
                .items(new ArrayList<>())
                .build();

        when(requestRepo.findById(1L)).thenReturn(Optional.of(request));

        ItemRequestResponseDto dto = service.getRequestById(1L);
        assertEquals("Описание", dto.getDescription());
    }
}
