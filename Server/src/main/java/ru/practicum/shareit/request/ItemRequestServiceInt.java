package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;

import java.util.List;

public interface ItemRequestServiceInt {

    RequestShortDto createRequest(Long userId, ItemRequestCreateDto dto);

    List<ItemRequestResponseDto> getUserRequests(Long userId);

    ItemRequestResponseDto getRequestById(Long requestId);

    List<ItemRequestResponseDto> getAllRequests();
}
