package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;


import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public RequestShortDto createRequest(@RequestBody ItemRequestCreateDto dto,
                                         @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestService.createRequest(userId, dto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getUserRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequests() {
        return itemRequestService.getAllRequests();
    }
}