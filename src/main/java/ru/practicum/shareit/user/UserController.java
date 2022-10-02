package ru.practicum.shareit.user;

import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.Collection;

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
        return userService.getAllUsers();
    }

    @PostMapping
    UserDto createUser(@Validated(Create.class) @RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping(path = "/{id}")
    UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PatchMapping(path = "/{id}")
    UserDto updateUser(@Validated(Update.class) @RequestBody UserDto user, @PathVariable Long id) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping(path = "/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
