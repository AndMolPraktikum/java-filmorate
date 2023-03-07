package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
        log.info("Входящий запрос GET /users. Исходящий ответ: {}", users.values());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Входящий запрос POST: {}", user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("Исходящий ответ: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        log.info("Входящий запрос PUT: {}", user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        Integer key = user.getId();
        if (users.containsKey(key)) {
            users.put(key, user);
            log.info("Исходящий ответ: {}", user);
            return users.get(key);
        } else {
            log.error("Пользователь с таким ID не существует: {}", user.getId());
            throw new UserValidationException("Пользователь с таким ID не существует");
        }
    }
}

