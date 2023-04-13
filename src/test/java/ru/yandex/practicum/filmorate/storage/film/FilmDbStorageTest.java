package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
                .friendIds(new ArrayList<>())
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
        Film createHarry = filmStorage.create(harryPotter);

        assertThat(createHarry)
                .hasFieldOrPropertyWithValue("name", "Гарри Поттер и философский камень");
    }

    @Test
    void shouldBeReturnedEmptyForFilm99() {
        FilmNotFoundException exc = Assertions.assertThrows(FilmNotFoundException.class, () -> {
            filmStorage.get(99);
        }, "Фильм с ID 99 не существует");
        Assertions.assertEquals("Фильм с ID 99 не существует", exc.getMessage());
    }

    @Test
    void shouldBeCreateAndDeleteFilm() {
        Film laputaCreate = filmStorage.create(laputa);

        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .isNotEmpty()
                .extracting("name")
                .contains("Небесный замок Лапута");

        filmStorage.remove(laputaCreate.getId());

        allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .extracting("name")
                .doesNotContain("Небесный замок Лапута");
    }

    @Test
    void shouldBeUpdateFilm() {
        filmStorage.create(harryPotter);

        harryPotter.setId(1);
        harryPotter.setName("Гарри Поттер и тайная комната");
        Film harryUpdate = filmStorage.update(1, harryPotter);
        assertThat(harryUpdate)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Гарри Поттер и тайная комната");
    }

    @Test
    void shouldBeSetLikeToFilm() {
        Film film = filmStorage.create(harryPotter);
        User user = userStorage.create(stevenSpielberg);

        assertThat(film).hasFieldOrPropertyWithValue("likes", 0L);
        filmStorage.like(film.getId(), user.getId());

        Film filmLikePlusOptional = filmStorage.get(film.getId());
        assertThat(filmLikePlusOptional).hasFieldOrPropertyWithValue("likes", 1L);
    }

    @Test
    void shouldBeRemoveLikeFilm() {
        Film film = filmStorage.create(harryPotter);
        User user = userStorage.create(stevenSpielberg);

        assertThat(film).hasFieldOrPropertyWithValue("likes", 0L);
        filmStorage.like(film.getId(), user.getId());

        Film filmLikePlusOptional = filmStorage.get(film.getId());
        assertThat(filmLikePlusOptional).hasFieldOrPropertyWithValue("likes", 1L);

        filmStorage.removeLike(film.getId(), user.getId());
        filmLikePlusOptional = filmStorage.get(film.getId());
        assertThat(filmLikePlusOptional).hasFieldOrPropertyWithValue("likes", 0L);
    }
}