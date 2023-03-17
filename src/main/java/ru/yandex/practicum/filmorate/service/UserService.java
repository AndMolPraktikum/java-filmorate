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

    public User findUserById(long id) {
        return inMemoryUserStorage.findUserById(id);
    }

    public User create(User user) {
        if (findAll().stream().anyMatch(p -> p.getEmail().equals(user.getEmail()))) {
            log.error("Пользователь с Email {} уже существует", user.getEmail());
            throw new UserAlreadyExistException(String.format("Пользователь с " +
                    "Email: %s уже существует", user.getEmail()));
        }
        return inMemoryUserStorage.create(user);
    }

    public User update(long key, User user) {
        return inMemoryUserStorage.update(key, user);
    }

    public void remove(long key) {
        inMemoryUserStorage.remove(key);
    }

    //добавление в друзья
    //***То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены***
    public void addToFriends(long id, long friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            log.error("Пользователь с ID {} не существует", id);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", id));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            log.error("Пользователь с ID {} не существует", friendId);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", friendId));
        }
        inMemoryUserStorage.getUsers().get(id).getFriendsId().add(friendId);
        inMemoryUserStorage.getUsers().get(friendId).getFriendsId().add(id);
    }

    //удаление из друзей
    public void removeFromFriends(long id, long friendId) {
        inMemoryUserStorage.getUsers().get(id).getFriendsId().remove(friendId);
        inMemoryUserStorage.getUsers().get(friendId).getFriendsId().remove(id);
    }

    //вывод списка общих друзей
    public List<User> findAllFriends(long id) {
        Set<Long> friendsId = inMemoryUserStorage.getUsers().get(id).getFriendsId();
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            friends.add(inMemoryUserStorage.getUsers().get(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(long id, long otherId) {
        Set<Long> friendsId = inMemoryUserStorage.getUsers().get(id).getFriendsId();
        Set<Long> friendsOtherId = inMemoryUserStorage.getUsers().get(otherId).getFriendsId();
        Set<Long> commonFriends = mutualValues(friendsId, friendsOtherId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : commonFriends) {
            friends.add(inMemoryUserStorage.getUsers().get(friendId));
        }
        return friends;
    }

    private Set<Long> mutualValues(Set<Long> set1, Set<Long> set2) {
        Set<Long> newSet = new HashSet<>(set1);
        newSet.retainAll(set2);
        return newSet;
    }
}
