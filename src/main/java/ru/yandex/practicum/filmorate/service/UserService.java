package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.Status;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.enums.Status.CONFIRMED;
import static ru.yandex.practicum.filmorate.enums.Status.UNCONFIRMED;

@Slf4j
@Service
public class UserService {

    private final UserStorage userDbStorage;

    @Autowired
    public UserService(UserStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public Collection<User> findAll() { //TODO it's work
        return userDbStorage.findAll();
    }

    public User get(long id) {  //TODO it's work
        Optional<User> userOptional = userDbStorage.get(id);
        if (userOptional.isEmpty()) {
            log.error("Пользователь с ID {} не существует", id);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", id));
        }
        return userOptional.get();
    }

    public User create(User user) {  //TODO it's work
        if (findAll().stream().anyMatch(p -> p.getEmail().equals(user.getEmail()))) {
            log.error("Пользователь с Email {} уже существует", user.getEmail());
            throw new UserAlreadyExistException(String.format("Пользователь с " +
                    "Email: %s уже существует", user.getEmail()));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDbStorage.create(user).get();
    }

    public User update(long key, User user) {  //TODO it's work
        Optional<User> userOptional = userDbStorage.get(key);
        if (userOptional.isEmpty()) {
            log.error("Пользователя с ID {} не существует", key);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", key));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        //предполагается, что update затрагивает только данные user, список друзей не корректируется
        return userDbStorage.update(key, user).get();
    }

    public void remove(long key) {
        userDbStorage.remove(key);
    }

    //добавление в друзья
    //*** дружба должна стать односторонней. Это значит, что если какой-то пользователь оставил
    // вам заявку в друзья, то он будет в списке ваших друзей, а вы в его — нет.***
    // User id подает заявку на дружбу с User friendId, у id есть друг, а у friendId нет.
    public void addToFriends(long id, long friendId) {  //TODO it's work
        Optional<User> userOptional = userDbStorage.get(id);
        if (userOptional.isEmpty()) {
            log.error("Пользователь с ID {} не существует", id);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", id));
        }
        userOptional = userDbStorage.get(friendId);
        if (userOptional.isEmpty()) {
            log.error("Пользователь с ID {} не существует", friendId);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", friendId));
        }
        userDbStorage.createFriendship(id, friendId);
    }

    //удаление из друзей
    public void removeFromFriends(long userid, long friendId) {   //TODO it's work
        Optional<Status> statusOptional = get(userid).getFriends().stream()
                .filter(friend -> friend.getId() == friendId)
                .map(Friend::getStatus)
                .findFirst();
        if (statusOptional.isEmpty()) {
            log.error("Пользователь ID: {} не имеет связи с пользователем ID: {} ", userid, friendId);
        }
        if (statusOptional.get().equals(UNCONFIRMED)) {
            userDbStorage.removeFromFriends(userid, friendId);
        }
        if (statusOptional.get().equals(CONFIRMED)) {
            userDbStorage.removeFromFriends(userid, friendId);
            userDbStorage.removeFromFriends(friendId, userid);
        }
    }

    //вывод списка общих друзей
    public List<User> findAllFriends(long id) {  //TODO it's work
        return get(id).getFriends().stream()
                .map(Friend::getId)
                .map(this::get)
                .collect(Collectors.toList());
    }

   public List<User> findCommonFriends(long id, long otherId) {  //TODO it's work
//        Set<Long> friendsId = get(id).getFriends();
//        Set<Long> friendsOtherId = get(otherId).getFriends();
//        Set<Long> commonFriends = mutualValues(friendsId, friendsOtherId);
//        List<User> friends = new ArrayList<>();
//        for (Long friendId : commonFriends) {
//            friends.add(get(friendId));
//        }
        return get(id).getFriends().stream()
                .filter(get(otherId).getFriends()::contains)
                .map(Friend::getId)
                .map(this::get)
                .collect(Collectors.toList());
    }

}
