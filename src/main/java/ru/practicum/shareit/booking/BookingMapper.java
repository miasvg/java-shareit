package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {

    public static Booking toBooking(BookingDto dto, Item item, User booker) {
        return new Booking(null, dto.getStart(), dto.getEnd(), item, booker, BookingStatus.WAITING);
    }

    public static BookingResponseDto toResponseDto(Booking booking, ItemDto itemDto, UserDto bookerDto) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDto,
                bookerDto,
                booking.getStatus()
        );
    }
}
