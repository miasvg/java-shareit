package ru.practicum.shareit.user;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceInt userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Creating user: {}", userDto);
        return UserMapper.toUserDto(userService.createUser(UserMapper.toUser(userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody @Validated (UserDto.OnUpdate.class)  UserDto userDto) {
        log.info("Updating user: {}", userDto);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Getting user: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Getting all users");
        return userService.getAllUsers();

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Deleting user: {}", userId);
        userService.deleteUser(userId);
    }
}