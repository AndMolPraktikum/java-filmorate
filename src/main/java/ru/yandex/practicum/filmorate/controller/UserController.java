package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> findAll() {
        log.info("Входящий запрос GET /users. Исходящий ответ: {}", userService.findAll());
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("Входящий запрос GET /users/{}. Исходящий ответ: {}", id, userService.findUserById(id));
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Long id) {
        log.info("Входящий запрос GET /users/{}/friends. Исходящий ответ: {}", id, userService.findAllFriends(id));
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> commonFriends = userService.findCommonFriends(id, otherId);
        log.info("Входящий запрос GET /users/{}/friends/common/{}. " +
                "Исходящий ответ: {}", id, otherId, commonFriends);
        return commonFriends;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User responseUser = userService.create(user);
        log.info("Входящий запрос POST: {}, Исходящий ответ: {}", user, responseUser);
        return responseUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        long key = user.getId();
        User responseUser = userService.update(key, user);
        log.info("Входящий запрос PUT: {}, Исходящий ответ: {}", user, responseUser);
        return responseUser;
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addToFriends(id, friendId);
        log.info("Входящий запрос PUT /users/{}/friends/{}", id, friendId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        userService.remove(id);
        log.info("Входящий запрос DELETE /users, c ID: {}", id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFromFriends(id, friendId);
        log.info("Входящий запрос DELETE /users/{}/friends/{}", id, friendId);
    }
}

