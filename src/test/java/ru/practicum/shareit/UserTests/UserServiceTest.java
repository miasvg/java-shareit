package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class UserServiceTest {

    @Mock
    private UserRepo userRepository;  // Мок репозитория

    @InjectMocks
    private UserService userService;  // UserService с внедрённым моком

    @Test
    public void testCreateUser() {
        // Устанавливаем данные для теста
        UserDto userDto = new UserDto(null, "Mia", "mia@gmail.com");
        User savedUser = new User(1L, "Mia", "mia@gmail.com");

        when(userRepository.existsByEmail("mia@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mia", result.getName());
        assertEquals("mia@gmail.com", result.getEmail());

        // Проверяем, что методы мока были вызваны
        verify(userRepository).existsByEmail("mia@gmail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserDto updateDto = new UserDto(null, "New Mia", "newmia@gmail.com");
        User existingUser = new User(userId, "Old Mia", "oldmia@gmail.com");


        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("newmia@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.updateUser(userId, updateDto);

        assertEquals("New Mia", result.getName());
        assertEquals("newmia@gmail.com", result.getEmail());
    }
    @Test
    void getUserById_WhenUserExists_ReturnsUserDto() {
        Long userId = 1L;
        User user = new User(userId, "Alice", "alice@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void getAllUsers_ReturnsListOfUserDtos() {
        User user1 = new User(1L, "Alice", "alice@example.com");
        User user2 = new User(2L, "Bob", "bob@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
    }

    @Test
    void deleteUser_WhenUserExists_DeletesSuccessfully() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}
