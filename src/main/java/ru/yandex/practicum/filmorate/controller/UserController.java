package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws UserValidationException {
        user = isValid(user);
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("Объект для сохранения User: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        Integer key = user.getId();
        if (users.containsKey(key)) {
            users.get(key).setEmail(user.getEmail());
            users.get(key).setLogin(user.getLogin());
            users.get(key).setName(user.getName());
            users.get(key).setBirthday(user.getBirthday());
            log.info("Объект для обновления User: {}", user);
            return users.get(key);
        } else {
            throw new UserValidationException("Пользователь с таким ID не существует");
        }
    }

    private User isValid(User user) throws UserValidationException {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с таким ID уже существует: {}", user.getId());
            throw new UserValidationException("Пользователь с таким ID уже существует");
        }
        if (user.getLogin().contains(" ")) {
            log.info("Логин содержит пробел: {}", user.getLogin());
            throw new UserValidationException("Логин содержит пробел");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
