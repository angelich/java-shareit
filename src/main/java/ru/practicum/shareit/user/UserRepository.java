package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;

/**
 * Хранилище пользователей
 */
@Repository
public class UserRepository {
    private static Long counter = 0L;
    private final HashMap<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    private static Long incrementCounter() {
        return ++counter;
    }

    public User createUser(User user) {
        Long newId = incrementCounter();
        user.setId(newId);
        users.put(newId, user);
        return users.get(newId);
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public User update(Long id, User user) {
        User updatedUser = users.get(id);
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        return updatedUser;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}
