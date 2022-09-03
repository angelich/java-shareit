package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Сервис по работе с пользователями
 */
public interface UserService {
    Collection<User> getAllUsers();

    User createUser(UserDto user);

    User getUser(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, UserDto user);
}
