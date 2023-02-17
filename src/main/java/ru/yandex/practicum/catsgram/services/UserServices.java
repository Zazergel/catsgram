package ru.yandex.practicum.catsgram.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exceptions.InvalidEmailException;
import ru.yandex.practicum.catsgram.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.catsgram.exceptions.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServices {
    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    public Collection<User> findAll() {
        log.debug("Количество пользователей всего: {}", users.size());
        return users.values();
    }

    public User createUser(User user) {
        checkEmail(user);
        if (users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        users.put(user.getEmail(), user);
        log.info("Создан новый пользователь: " + user);
        return user;
    }

    public User updateUser(User user) {
        checkEmail(user);
        users.put(user.getEmail(), user);
        log.info("Обновлены данные пользователя: " + user);
        return user;
    }

    public User findUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("Пользователя с такой почтой не существует!");
        }
        return users.get(email);
    }

    public User findUserByName(String name) {
        return users.values().stream()
                .filter(p -> p.getNickname().equals(name))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format
                        ("Пользователь с именем № %d не найден", name)));
    }

    private void checkEmail(User user) {
        if (user.getEmail().isBlank()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
    }
}