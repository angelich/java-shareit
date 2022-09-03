package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUserDto;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    Collection<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    UserDto createUser(@Valid @RequestBody UserDto user) {
        return toUserDto(userService.createUser(user));
    }

    @GetMapping(path = "/{id}")
    UserDto getUser(@PathVariable Long id) {
        return toUserDto(userService.getUser(id));
    }

    @PatchMapping(path = "/{id}")
    UserDto updateUser(@Valid @RequestBody UserDto user, @PathVariable Long id) {
        return toUserDto(userService.updateUser(id, user));
    }

    @DeleteMapping(path = "/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
