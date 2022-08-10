package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
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
    Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(path = "/{id}")
    User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PatchMapping(path = "/{id}")
    User updateUser(@Valid @RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping(path = "/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
