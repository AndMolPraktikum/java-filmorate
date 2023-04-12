package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;
    private User chrisColumbus;
    private User peterJackson;

    @BeforeEach
    protected void init() {
        chrisColumbus = User.builder()
                .email("kris1958@yandex.ru")
                .login("kris1958")
                .name("Крис Коламбус")
                .birthday(LocalDate.of(1958, 9, 10))
                .friendIds(new ArrayList<>())
                .build();

        peterJackson = User.builder()
                .email("peter1961@yandex.ru")
                .login("peter1961")
                .name("Питер Джексон")
                .birthday(LocalDate.of(1961, 10, 31))
                .friendIds(new ArrayList<>())
                .build();
    }

    @Test
    void shouldBeReturnedAllUsers() {
        userStorage.create(peterJackson);
        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers)
                .isNotEmpty()
                .extracting("name")
                .contains("Питер Джексон")
                .doesNotContainNull();
    }

    @Test
    void shouldBeReturnedUser() {
        User chrisCreate = userStorage.create(chrisColumbus);

        assertThat(chrisCreate).hasFieldOrPropertyWithValue("name", "Крис Коламбус");
    }

    @Test
    void shouldBeReturnedEmptyForUser99() {
        UserNotFoundException exc = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userStorage.get(99);
        }, "Пользователь с ID 99 не существует");
        Assertions.assertEquals("Пользователь с ID 99 не существует", exc.getMessage());
    }

    @Test
    void shouldBeCreateAndDeleteUser() {
        User quentinTarantino = User.builder()
                .email("quentin1963@yandex.ru")
                .login("justQuentin")
                .name("Квентин Тарантино")
                .birthday(LocalDate.of(1963, 3, 27))
                .friendIds(new ArrayList<>())
                .build();

        User quentinCreate = userStorage.create(quentinTarantino);

        assertThat(quentinCreate).hasFieldOrPropertyWithValue("name", "Квентин Тарантино");

        userStorage.remove(quentinCreate.getId());

        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers)
                .extracting("name")
                .doesNotContain("Квентин Тарантино");
    }

    @Test
    void shouldBeUpdateUser() {
        chrisColumbus = userStorage.create(chrisColumbus);
        chrisColumbus.setName("Кристофер Джозеф Коламбус");
        User chrisUpdate = userStorage.update(chrisColumbus.getId(), chrisColumbus);

        assertThat(chrisUpdate.getName()).contains("Джозеф");
    }

    @Test
    void shouldCreateFriendshipAndRemoveFromFriends() {
        User jamesCameron = User.builder()
                .email("quentin1963@yandex.ru")
                .login("james1954")
                .name("Джеймс Кэмерон")
                .birthday(LocalDate.of(1954, 8, 16))
                .friendIds(new ArrayList<>())
                .build();
        User jamesCreate = userStorage.create(jamesCameron);

        User georgeLucas = User.builder()
                .email("georluc@yandex.ru")
                .login("georluc")
                .name("Джордж Лукас")
                .birthday(LocalDate.of(1944, 5, 14))
                .friendIds(new ArrayList<>())
                .build();
        User georgeCreate = userStorage.create(georgeLucas);

        userStorage.createFriendship(jamesCreate.getId(), georgeCreate.getId());

        jamesCreate = userStorage.get(jamesCreate.getId());
        assertThat(jamesCreate.getFriendIds()).hasSize(1);

        userStorage.removeFromFriends(jamesCreate.getId(), georgeCreate.getId());

        jamesCreate = userStorage.get(jamesCreate.getId());
        assertThat(jamesCreate.getFriendIds()).hasSize(0);
    }
}