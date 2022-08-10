package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Сервис по работе с пользователями
 *
 * @author angelich:
 * @since 10.08.2022
 */
@Service
public class UserService {
    private final UserDao userDao;
    private static final Pattern EMAIL_PATTERN = compile("^(.+)@(\\S+)$");

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Collection<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User createUser(User user) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            throw new IllegalArgumentException("Email should be provided");
        }
        checkEmailValid(user);
        checkEmailAlreadyExist(user);
        return userDao.createUser(user);
    }

    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    public User updateUser(Long id, User user) {
        if (user.getEmail() != null) {
            checkEmailValid(user);
            checkEmailAlreadyExist(user);
        }
        return userDao.update(id, user);
    }

    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    private static void checkEmailValid(User user) {
        boolean isEmailValid = EMAIL_PATTERN.matcher(user.getEmail()).matches();
        if (!isEmailValid) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void checkEmailAlreadyExist(User user) {
       boolean isEmailAlreadyExist = userDao.getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
       if (isEmailAlreadyExist){
           throw new IllegalStateException("Email is already exists");
       }
    }
}
