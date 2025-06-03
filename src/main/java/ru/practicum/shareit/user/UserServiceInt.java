package ru.practicum.shareit.user;

import java.util.List;

public interface UserServiceInt {

    User createUser(User user);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);

}
