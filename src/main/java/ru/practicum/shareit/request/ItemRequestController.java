package ru.practicum.shareit.request;

import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;


import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestShortDto createRequest(@RequestBody @Valid ItemRequestCreateDto dto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createRequest(userId, dto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
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
