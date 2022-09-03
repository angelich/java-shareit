package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static ru.practicum.shareit.user.UserMapper.toUser;

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
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User createUser(UserDto user) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            throw new IllegalArgumentException("Email should be provided");
        }
        checkEmailValid(user);
        checkEmailAlreadyExist(user);
        return userRepository.createUser(toUser(user));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.getUser(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public User updateUser(Long id, UserDto user) {
        if (user.getEmail() != null) {
            checkEmailValid(user);
            checkEmailAlreadyExist(user);
        }
        return userRepository.update(id, toUser(user));
    }

    private void checkEmailAlreadyExist(UserDto user) {
        boolean isEmailAlreadyExist = userRepository.getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
        if (isEmailAlreadyExist) {
            throw new IllegalStateException("Email is already exists");
        }
    }
}
