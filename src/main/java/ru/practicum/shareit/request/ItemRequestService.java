package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserServiceInt;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService implements ItemRequestServiceInt {
    private final ItemRequestRepo requestRepository;
    private final UserServiceInt userService;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto, Long requestorId) {
        userService.getUserById(requestorId); // Проверяем, что пользователь существует
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, new User(requestorId, null, null));
        ItemRequest savedRequest = requestRepository.save(request);
        return ItemRequestMapper.toItemRequestDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long requestorId) {
        userService.getUserById(requestorId);
        return requestRepository.findAllByRequestorId(requestorId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, int from, int size) {
        userService.getUserById(userId);
        return requestRepository.findAllExceptRequestor(userId).stream()
                .skip(from)
                .limit(size)
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        userService.getUserById(userId);
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        return ItemRequestMapper.toItemRequestDto(request);
    }
}