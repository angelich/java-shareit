package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserIntegrationTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    void saveUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.com");
        userService.createUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        UserDto savedUser = UserMapper.toUserDto(user);
        assertThat(savedUser.getId(), notNullValue());
        assertThat(savedUser.getName(), equalTo(userDto.getName()));
        assertThat(savedUser.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.com");

        userDto = userService.createUser(userDto);
        userDto.setName("updatedName");
        userDto.setEmail("updatedEmail");

        userService.updateUser(userDto.getId(), userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query
                .setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }
}
