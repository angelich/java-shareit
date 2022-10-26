package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;


    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(1L, "name", "email@email.ru"));
        user2 = userRepository.save(new User(2L, "name2", "email2@email.ru"));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void findUserByEmail() {
        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }
}
