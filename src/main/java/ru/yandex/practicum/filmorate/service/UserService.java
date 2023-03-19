package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User get(long id) {
        if (!inMemoryUserStorage.isContains(id)) {
            log.error("Пользователь с ID {} не существует", id);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", id));
        }
        return inMemoryUserStorage.get(id);
    }

    public User create(User user) {
        if (findAll().stream().anyMatch(p -> p.getEmail().equals(user.getEmail()))) {
            log.error("Пользователь с Email {} уже существует", user.getEmail());
            throw new UserAlreadyExistException(String.format("Пользователь с " +
                    "Email: %s уже существует", user.getEmail()));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return inMemoryUserStorage.create(user);
    }

    public User update(long key, User user) {
        if (!inMemoryUserStorage.isContains(key)) {
            log.error("Пользователя с ID {} не существует", key);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", key));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getFriendsId() == null) {
            user.setFriendsId(new HashSet<>());
        }
        return inMemoryUserStorage.update(key, user);
    }

    public void remove(long key) {
        inMemoryUserStorage.remove(key);
    }

    //добавление в друзья
    //***То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены***
    public void addToFriends(long id, long friendId) {
        if (!inMemoryUserStorage.isContains(id)) {
            log.error("Пользователь с ID {} не существует", id);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", id));
        }
        if (!inMemoryUserStorage.isContains(friendId)) {
            log.error("Пользователь с ID {} не существует", friendId);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", friendId));
        }
        User userId = get(id);
        userId.getFriendsId().add(friendId);
        update(id, userId);

        User userFriendId = get(friendId);
        userFriendId.getFriendsId().add(id);
        update(friendId, userFriendId);
    }

    //удаление из друзей
    public void removeFromFriends(long id, long friendId) {
        User userId = get(id);
        userId.getFriendsId().remove(friendId);
        update(id, userId);

        User userFriendId = get(id);
        userFriendId.getFriendsId().remove(id);
        update(id, userFriendId);
    }

    //вывод списка общих друзей
    public List<User> findAllFriends(long id) {
        Set<Long> friendsId = get(id).getFriendsId();
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            friends.add(get(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(long id, long otherId) {
        Set<Long> friendsId = get(id).getFriendsId();
        Set<Long> friendsOtherId = get(otherId).getFriendsId();
        Set<Long> commonFriends = mutualValues(friendsId, friendsOtherId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : commonFriends) {
            friends.add(get(friendId));
        }
        return friends;
    }

    private Set<Long> mutualValues(Set<Long> set1, Set<Long> set2) {
        Set<Long> newSet = new HashSet<>(set1);
        newSet.retainAll(set2);
        return newSet;
    }
}
