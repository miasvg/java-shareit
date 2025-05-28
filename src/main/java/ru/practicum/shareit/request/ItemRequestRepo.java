package ru.practicum.shareit.request;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepo {

    ItemRequest save(ItemRequest request);

    Optional<ItemRequest> findById(Long id);

    List<ItemRequest> findAllByRequestorId(Long requestorId);

    List<ItemRequest> findAllExceptRequestor(Long requestorId);

}
