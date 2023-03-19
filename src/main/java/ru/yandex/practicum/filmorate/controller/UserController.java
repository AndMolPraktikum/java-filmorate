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
        log.info("Входящий запрос GET /users.");
        Collection<User> allUsers = userService.findAll();
        log.info("Исходящий ответ: {}", allUsers);
        return allUsers;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info("Входящий запрос GET /users/{}.", id);
        User userById = userService.get(id);
        log.info("Исходящий ответ: {}", userById);
        return userById;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable Long id) {
        log.info("Входящий запрос GET /users/{}/friends.", id);
        Collection<User> allFriends = userService.findAllFriends(id);
        log.info("Исходящий ответ: {}", allFriends);
        return allFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Входящий запрос GET /users/{}/friends/common/{}.", id, otherId);
        Collection<User> commonFriends = userService.findCommonFriends(id, otherId);
        log.info("Исходящий ответ: {}", commonFriends);
        return commonFriends;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Входящий запрос POST /users: {}", user);
        User createdUser = userService.create(user);
        log.info("Исходящий ответ: {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        log.info("Входящий запрос PUT /users: {}", user);
        long key = user.getId();
        User updatedUser = userService.update(key, user);
        log.info("Исходящий ответ: {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Входящий запрос PUT /users/{}/friends/{}", id, friendId);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        log.info("Входящий запрос DELETE /users, c ID: {}", id);
        userService.remove(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Входящий запрос DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFromFriends(id, friendId);
    }
}

