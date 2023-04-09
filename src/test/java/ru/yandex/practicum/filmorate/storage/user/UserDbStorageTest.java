package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;

    private User correctUser;
    private User incorrectUser;

    @BeforeEach
    protected void init() {
        correctUser = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1946, 8, 20))
                .friends(new ArrayList<>())
                .build();

        incorrectUser = User.builder()
                .email("yandex@mail.ru")
                .login("dolore ullamco")
                .birthday(LocalDate.of(2446, 8, 20))
                .friends(new ArrayList<>())
                .build();
    }

    @Test
    void shouldBeReturnedAllUsers() {
        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers)
                .isNotEmpty()
                .hasSize(7)
                .doesNotContainNull();
    }

    @Test
    void shouldBeReturnedUser5() {
        Optional<User> userOptional = userStorage.get(5);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 5L)
                );
    }

    @Test
    void shouldBeReturnedEmptyForUser99() {
        Optional<User> userOptional = userStorage.get(99);

        assertThat(userOptional)
                .isEmpty();
    }

    @Test
    void shouldBeCreateAndDeleteUser8() {
        Optional<User> userOptional = userStorage.create(correctUser);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("name", "Nick Name")
                                .hasFieldOrPropertyWithValue("id", 8L)
                );

        userStorage.remove(8);

        Collection<User> allUsers = userStorage.findAll();
        assertThat(allUsers)
                .hasSize(7);
    }

    @Test
    void update() {
        correctUser.setId(6);
        correctUser.setName("Обновленный персонаж");
        Optional<User> userOptional = userStorage.update(6, correctUser);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("name", "Обновленный персонаж")
                                .hasFieldOrPropertyWithValue("id", 6L)
                );
    }

    @Test
    void shouldCreateFriendshipAndRemoveFromFriends() {
        Optional<User> userOptional = userStorage.get(1);
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends())
                                .hasSize(0)
                );

        userStorage.createFriendship(1, 2);

        userOptional = userStorage.get(1);
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends())
                                .hasSize(1)
                );

        userStorage.removeFromFriends(1, 2);

        userOptional = userStorage.get(1);
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends())
                                .hasSize(0)
                );
    }
}