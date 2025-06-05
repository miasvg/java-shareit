package ru.practicum.shareit.bookingTests;

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
import ru.practicum.shareit.item.ItemRepo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserServiceInt;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepo bookingRepo;
    @Mock
    private ItemRepo itemRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserServiceInt userService;

    private User user;
    private User owner;
    private Item item;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "User", "u@mail.com");
        owner = new User(2L, "Owner", "o@mail.com");

        item = new Item(1L, "Item", "Desc", true, owner.getId(), null);

        bookingDto = new BookingDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                item.getId()
        );
    }

    @Test
    void createBooking_shouldCreate() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        Booking saved = new Booking(10L, bookingDto.getStart(), bookingDto.getEnd(), item, user, BookingStatus.WAITING);
        when(bookingRepo.save(any())).thenReturn(saved);

        BookingResponseDto result = bookingService.createBooking(1L, bookingDto);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void updateBookingStatus_shouldApprove() throws Exception {
        Booking booking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(), item, user, BookingStatus.WAITING);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.updateBookingStatus(owner.getId(), 1L, true);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void getBooking_shouldReturnBooking() {
        Booking booking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(), item, user, BookingStatus.WAITING);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.getBooking(1L, user.getId());

        assertThat(result.getId()).isEqualTo(1L);
    }
}

