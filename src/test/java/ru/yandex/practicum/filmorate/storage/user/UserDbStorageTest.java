package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
                .friends(new ArrayList<>())
                .build();

        peterJackson = User.builder()
                .email("peter1961@yandex.ru")
                .login("peter1961")
                .name("Питер Джексон")
                .birthday(LocalDate.of(1961, 10, 31))
                .friends(new ArrayList<>())
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
        Optional<User> userOptional = userStorage.create(chrisColumbus);
        Optional<User> getOptional = userStorage.get(userOptional.get().getId());

        assertThat(getOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Крис Коламбус")
                );
    }

    @Test
    void shouldBeReturnedEmptyForUser99() {
        Optional<User> userOptional = userStorage.get(99);

        assertThat(userOptional)
                .isEmpty();
    }

    @Test
    void shouldBeCreateAndDeleteUser() {
        User quentinTarantino = User.builder()
                .email("quentin1963@yandex.ru")
                .login("justQuentin")
                .name("Квентин Тарантино")
                .birthday(LocalDate.of(1963, 3, 27))
                .friends(new ArrayList<>())
                .build();

        Optional<User> userOptional = userStorage.create(quentinTarantino);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("name", "Квентин Тарантино")
                );

        userStorage.remove(userOptional.get().getId());

        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers)
                .extracting("name")
                .doesNotContain("Квентин Тарантино");
    }

    @Test
    void shouldBeUpdateUser() {
        Optional<User> chrisOptional = userStorage.create(chrisColumbus);
        chrisColumbus = chrisOptional.get();
        chrisColumbus.setName("Кристофер Джозеф Коламбус");
        chrisOptional = userStorage.update(chrisColumbus.getId(), chrisColumbus);

        assertThat(chrisOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getName())
                                .contains("Джозеф")
                );
    }

    @Test
    void shouldCreateFriendshipAndRemoveFromFriends() {
        User jamesCameron = User.builder()
                .email("quentin1963@yandex.ru")
                .login("james1954")
                .name("Джеймс Кэмерон")
                .birthday(LocalDate.of(1954, 8, 16))
                .friends(new ArrayList<>())
                .build();
        Optional<User> jamesOptional = userStorage.create(jamesCameron);

        User georgeLucas = User.builder()
                .email("georluc@yandex.ru")
                .login("georluc")
                .name("Джордж Лукас")
                .birthday(LocalDate.of(1944, 5, 14))
                .friends(new ArrayList<>())
                .build();
        Optional<User> georgeOptional = userStorage.create(georgeLucas);

        userStorage.createFriendship(jamesOptional.get().getId(), georgeOptional.get().getId());

        jamesOptional = userStorage.get(jamesOptional.get().getId());
        assertThat(jamesOptional)
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends())
                                .hasSize(1)
                );

        userStorage.removeFromFriends(jamesOptional.get().getId(), georgeOptional.get().getId());

        jamesOptional = userStorage.get(jamesOptional.get().getId());
        assertThat(jamesOptional)
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends())
                                .hasSize(0)
                );
    }
}