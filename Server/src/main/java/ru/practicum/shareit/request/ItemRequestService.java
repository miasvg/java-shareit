package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;
import ru.practicum.shareit.user.*;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService implements ItemRequestServiceInt {

    private final ItemRequestRepository requestRepository;
    private final ItemRepo itemRepository;
    private final UserRepo userRepository;
    private final UserService userService;


    @Override
    @Transactional
    public RequestShortDto createRequest(Long userId, ItemRequestCreateDto dto) {
        User requestor = UserMapper.toUser(userService.getUserById(userId));
        ItemRequest request = ItemRequestMapper.toItemRequest(dto, requestor);
        request.setRequestor(requestor);
        return ItemRequestMapper.toShortDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getUserRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден")); // валидация существования
        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream().
                map(ItemRequestMapper::toItemRequestResponseDto)
                .sorted(Comparator.comparing(ItemRequestResponseDto::getCreated).reversed())
                .toList();
    }

    @Override
    @Transactional
    public ItemRequestResponseDto getRequestById(Long requestId){
        return ItemRequestMapper.toItemRequestResponseDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("запрос не найден")));
    }

    @Override
    @Transactional
    public List<ItemRequestResponseDto> getAllRequests(){
        List<ItemRequest> requests = requestRepository.findAll();
        return requests.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .sorted(Comparator.comparing(ItemRequestResponseDto::getCreated))
                .toList();
    }
}
