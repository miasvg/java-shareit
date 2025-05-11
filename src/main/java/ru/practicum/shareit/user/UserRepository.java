package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class UserRepository implements UserRepo {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToIdMap = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        users.put(user.getId(), user);
        emailToIdMap.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        User user = users.get(id);
        if (user != null) {
            emailToIdMap.remove(user.getEmail());
            users.remove(id);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return emailToIdMap.containsKey(email);
    }
}
