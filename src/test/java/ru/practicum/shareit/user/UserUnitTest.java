package ru.practicum.shareit.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserUnitTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldCreateUser() {
        UserDto dto = new UserDto();
        dto.setEmail("email@mail.com");
        dto.setName("userName");

        Mockito
                .when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(new User(1L, "userName", "email@mail.com"));

        UserDto savedUser = userService.createUser(dto);

        assertEquals(1L, savedUser.getId());
        assertEquals("userName", savedUser.getName());
        assertEquals("email@mail.com", savedUser.getEmail());
    }

    @Test
    void shouldUpdateUser() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("email@mail.com");
        dto.setName("userName");

        Mockito
                .when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(dto)));

        Mockito
                .when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(UserMapper.toUser(dto));

        UserDto updatedUser = userService.updateUser(1L, dto);

        assertEquals(dto.getId(), updatedUser.getId());
        assertEquals(dto.getName(), updatedUser.getName());
        assertEquals(dto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void shouldReturnAllUsers() {
        UserDto dto = new UserDto();
        dto.setEmail("email@mail.com");
        dto.setName("userName");

        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(new User(1L, "userName", "email@mail.com")));

        var foundUser = userService.getAllUsers().stream().findFirst().get();

        assertEquals(1L, foundUser.getId());
        assertEquals("userName", foundUser.getName());
        assertEquals("email@mail.com", foundUser.getEmail());
    }
}
