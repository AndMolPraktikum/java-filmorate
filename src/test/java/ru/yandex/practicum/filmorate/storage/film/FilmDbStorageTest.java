package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private Film harryPotter;
    private Film laputa;
    private User stevenSpielberg;

    @BeforeEach
    protected void init() {
        harryPotter = Film.builder()
                .name("Гарри Поттер и философский камень")
                .description("Фильм о мальчике, который выжил")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(152)
                .genres(Set.of(new Genre(1)))
                .mpa(new Mpa(1))
                .build();

        laputa = Film.builder()
                .name("Небесный замок Лапута")
                .description("Альтернативная реальность, соответствующая началу XX века.")
                .releaseDate(LocalDate.of(1986, 8, 2))
                .duration(125)
                .genres(Set.of(new Genre(3)))
                .mpa(new Mpa(1))
                .build();

        stevenSpielberg = User.builder()
                .email("stiven1946@yandex.ru")
                .login("Стивен Спилберг")
                .name("stiven1946")
                .birthday(LocalDate.of(1946, 12, 18))
                .friends(new ArrayList<>())
                .build();
    }

    @Test
    void shouldBeReturnedAllFilms() {
        filmStorage.create(harryPotter);
        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .isNotEmpty()
                .extracting("name")
                .contains("Гарри Поттер и философский камень")
                .doesNotContainNull();
    }

    @Test
    void shouldBeReturnedFilm() {
        Optional<Film> filmOptional = filmStorage.create(harryPotter);

        Optional<Film> getOptional = filmStorage.get(filmOptional.get().getId());

        assertThat(getOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Гарри Поттер и философский камень")
                );
    }

    @Test
    void shouldBeReturnedEmptyForFilm99() {
        Optional<Film> filmOptional = filmStorage.get(99);

        assertThat(filmOptional)
                .isEmpty();
    }

    @Test
    void shouldBeCreateAndDeleteFilm() {
        Optional<Film> filmOptional = filmStorage.create(laputa);

        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .isNotEmpty()
                .extracting("name")
                .contains("Небесный замок Лапута");

        filmStorage.remove(filmOptional.get().getId());

        allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .extracting("name")
                .doesNotContain("Небесный замок Лапута");
    }

    @Test
    void shouldBeUpdateFilm3() {
        filmStorage.create(harryPotter);

        harryPotter.setId(1);
        harryPotter.setName("Гарри Поттер и тайная комната");
        Optional<Film> filmOptional = filmStorage.update(1, harryPotter);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Гарри Поттер и тайная комната")
                );
    }

    @Test
    void shouldBeSetLikeToFilm4() {
        Optional<Film> filmOptional = filmStorage.create(harryPotter);
        Optional<User> userOptional = userStorage.create(stevenSpielberg);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 0L)
                );
        filmStorage.like(filmOptional.get().getId(), userOptional.get().getId());

        Optional<Film> filmLikePlusOptional = filmStorage.get(filmOptional.get().getId());
        assertThat(filmLikePlusOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 1L)
                );
    }

    @Test
    void shouldBeRemoveLikeFilm6() {
        Optional<Film> filmOptional = filmStorage.create(harryPotter);
        Optional<User> userOptional = userStorage.create(stevenSpielberg);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 0L)
                );
        filmStorage.like(filmOptional.get().getId(), userOptional.get().getId());

        Optional<Film> filmLikePlusOptional = filmStorage.get(filmOptional.get().getId());
        assertThat(filmLikePlusOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 1L)
                );

        filmStorage.removeLike(filmOptional.get().getId(), userOptional.get().getId());
        filmLikePlusOptional = filmStorage.get(filmOptional.get().getId());
        assertThat(filmLikePlusOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 0L)
                );
    }
}