package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userDbStorage;

    @Autowired
    public UserService(UserStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }

    public User get(long id) {
        return userDbStorage.get(id);
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
        return userDbStorage.create(user);
    }

    public User update(long key, User user) {
        userDbStorage.get(key);  // если юзера нет в БД, то выбросится исключение
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        //предполагается, что update затрагивает только данные user, список друзей не корректируется
        return userDbStorage.update(key, user);
    }

    public void remove(long key) {
        userDbStorage.remove(key);
    }

    public void addToFriends(long id, long friendId) {
        userDbStorage.get(id);  // если юзера нет в БД, то выбросится исключение
        userDbStorage.get(friendId);  // если друга нет в БД, то выбросится исключение
        userDbStorage.createFriendship(id, friendId);
    }

    public void removeFromFriends(long userId, long friendId) {    //удаление из друзей
        if (!get(userId).getFriendIds().contains(friendId)) {
            log.error("Пользователь с ID: {} не имеет в друзьях пользователя с ID: {} ", userId, friendId);
            throw new FriendNotFoundException(String.format("Пользователь с ID: %d не имеет " +
                    "в друзьях пользователя с ID: %d ", userId, friendId));
        }
        userDbStorage.removeFromFriends(userId, friendId);
    }

    public List<User> findAllFriends(long id) {
        return get(id).getFriendIds().stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(long id, long otherId) { //вывод списка общих друзей
        return get(id).getFriendIds().stream()
                .filter(get(otherId).getFriendIds()::contains)
                .map(this::get)
                .collect(Collectors.toList());
    }

}
