package ru.practicum.shareit.request;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class ItemRequestRepository {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private Long idCounter = 1L;

    public ItemRequest save(ItemRequest request) {
        if (request.getId() == null) {
            request.setId(idCounter++);
        }
        requests.put(request.getId(), request);
        return request;
    }

    public Optional<ItemRequest> findById(Long id) {
        return Optional.ofNullable(requests.get(id));
    }

    public List<ItemRequest> findAllByRequestorId(Long requestorId) {
        List<ItemRequest> result = new ArrayList<>();
        for (ItemRequest request : requests.values()) {
            if (request.getRequestor().getId().equals(requestorId)) {
                result.add(request);
            }
        }
        return result;
    }

    public List<ItemRequest> findAllExceptRequestor(Long requestorId) {
        List<ItemRequest> result = new ArrayList<>();
        for (ItemRequest request : requests.values()) {
            if (!request.getRequestor().getId().equals(requestorId)) {
                result.add(request);
            }
        }
        return result;
    }
}