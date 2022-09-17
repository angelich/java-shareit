package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * Сервис по работе с пользователями
 */
public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto getUser(Long id);

    void deleteUser(Long id);

    UserDto updateUser(Long id, UserDto user);
}
