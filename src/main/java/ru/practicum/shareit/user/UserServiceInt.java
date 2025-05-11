package ru.practicum.shareit.user;

import java.util.List;

public interface UserServiceInt {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);

    User getUserEntityById(Long userId);

}
