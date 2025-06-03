// ItemMapper.java
package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(ownerId);
        return item;
    }

    public static ItemWithBookingsAndCommentsDto toItemWithBookingsAndCommentsDto(Item item,
                                                                                  List<Booking> bookings, List<Comment> comments) {
        ItemWithBookingsAndCommentsDto dto = ItemWithBookingsAndCommentsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments.stream().map(CommentMapper::toDto).toList())
                .build();
        if (!bookings.isEmpty()) {
            dto.setLastBooking(BookingMapper.toShortDto(bookings.getLast()));
            if (bookings.size() > 1) {
                dto.setNextBooking(BookingMapper.toShortDto(bookings.get(bookings.size() - 2)));
            }
        }
        return dto;
    }
}
