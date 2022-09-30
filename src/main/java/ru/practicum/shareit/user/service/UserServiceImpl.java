package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

/**
 * Сервис по работе с пользователями
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = toUser(userDto);
        return toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUser(Long id) {
        return toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not exist")));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User updatedUser = toUser(userDto);
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not exist"));

        if (updatedUser.getName() != null) {
            savedUser.setName(updatedUser.getName());
        }

        String userEmail = updatedUser.getEmail();
        if (userEmail != null) {
            savedUser.setEmail(userEmail);
        }
        userRepository.save(savedUser);
        return toUserDto(savedUser);
    }

    @Override
    public void checkUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not exist"));
    }
}
