package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;
import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

/**
 * Сервис по работе с пользователями
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = compile("^(.+)@(\\S+)$");

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static void checkEmailValid(UserDto user) {
        boolean isEmailValid = EMAIL_PATTERN.matcher(user.getEmail()).matches();
        if (!isEmailValid) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto user) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            throw new IllegalArgumentException("Email should be provided");
        }
        checkEmailValid(user);
        checkEmailAlreadyExist(user);
        User createdUser = userRepository.createUser(toUser(user));
        return toUserDto(createdUser);
    }

    @Override
    public UserDto getUser(Long id) {
        return toUserDto(userRepository.getUser(id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        if (user.getEmail() != null) {
            checkEmailValid(user);
            checkEmailAlreadyExist(user);
        }
        User updatedUser = userRepository.update(id, toUser(user));
        return toUserDto(updatedUser);
    }

    private void checkEmailAlreadyExist(UserDto user) {
        boolean isEmailAlreadyExist = userRepository.getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
        if (isEmailAlreadyExist) {
            throw new IllegalStateException("Email is already exists");
        }
    }
}
