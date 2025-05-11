package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);

}
