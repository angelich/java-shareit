package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Сервис по работе с пользователями
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = compile("^(.+)@(\\S+)$");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User createUser(User user) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            throw new IllegalArgumentException("Email should be provided");
        }
        checkEmailValid(user);
        checkEmailAlreadyExist(user);
        return userRepository.createUser(user);
    }

    public User getUser(Long id) {
        return userRepository.getUser(id);
    }

    public User updateUser(Long id, User user) {
        if (user.getEmail() != null) {
            checkEmailValid(user);
            checkEmailAlreadyExist(user);
        }
        return userRepository.update(id, user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    private static void checkEmailValid(User user) {
        boolean isEmailValid = EMAIL_PATTERN.matcher(user.getEmail()).matches();
        if (!isEmailValid) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void checkEmailAlreadyExist(User user) {
       boolean isEmailAlreadyExist = userRepository.getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
       if (isEmailAlreadyExist){
           throw new IllegalStateException("Email is already exists");
       }
    }
}
