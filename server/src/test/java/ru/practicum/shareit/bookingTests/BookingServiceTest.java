package ru.practicum.shareit.bookingTests;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepo;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserServiceInt;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService service;

    @Mock
    private BookingRepo bookingRepo;
    @Mock
    private ItemRepo itemRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserServiceInt userService;

    private User booker;
    private Item item;
    private BookingDto bookingDto;

    @BeforeEach
    public void setup() {
        booker = new User(1L, "User", "user@mail.com");
        item = new Item(1L, "Item", "Desc", true, 2L, null);

        bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item.getId()
        );
    }

    @Test
    public void createBooking_whenValid_shouldReturnResponseDto() {
        when(userRepo.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepo.save(any())).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setId(10L);
            return b;
        });

        BookingResponseDto response = service.createBooking(booker.getId(), bookingDto);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    public void createBooking_whenItemUnavailable_shouldThrow() {
        item.setAvailable(false);
        when(userRepo.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> service.createBooking(booker.getId(), bookingDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("недоступна");
    }

    @Test
    public void updateBookingStatus_whenNotOwner_shouldThrowAccessDenied() {
        Booking booking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(), item, booker, BookingStatus.WAITING);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> service.updateBookingStatus(999L, 1L, true))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    public void getBooking_whenNotOwnerOrBooker_shouldThrow() {
        Booking booking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(), item, booker, BookingStatus.WAITING);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> service.getBooking(1L, 999L))
                .isInstanceOf(NotFoundException.class);
    }
}
