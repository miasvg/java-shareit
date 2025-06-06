package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserService;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        user = new User(1L, "John", "john@example.com");
        userDto = new UserDto(1L, "John", "john@example.com");
    }

    @Test
    void createUser_ShouldSaveNewUser() {
        when(userRepo.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void createUser_ShouldThrowConflict_WhenEmailExists() {
        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(userDto));
    }

    @Test
    void updateUser_ShouldUpdateNameAndEmail() {
        User updated = new User(1L, "Updated", "updated@example.com");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepo.save(any())).thenReturn(updated);

        UserDto toUpdate = new UserDto(null, "Updated", "updated@example.com");
        UserDto result = userService.updateUser(1L, toUpdate);

        assertEquals("Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void updateUser_ShouldThrowNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, userDto));
    }

    @Test
    void updateUser_ShouldThrowConflict_WhenEmailTaken() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail("other@example.com")).thenReturn(true);

        UserDto toUpdate = new UserDto(null, null, "other@example.com");

        assertThrows(ConflictException.class, () -> userService.updateUser(1L, toUpdate));
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void getUserById_ShouldThrowNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void deleteUser_ShouldCallDeleteById() {
        userService.deleteUser(1L);
        verify(userRepo).deleteById(1L);
    }
}
