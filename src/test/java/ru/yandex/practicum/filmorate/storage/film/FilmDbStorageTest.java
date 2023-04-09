package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    private Film correctFilm;

    @BeforeEach
    protected void init() {
        correctFilm = Film.builder()
                .name("Correct Movie")
                .description("A wonderful film about the life of programmers")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(1))
                .build();
    }

    @Test
    void shouldBeReturnedAllFilms() {
        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .isNotEmpty()
                .hasSize(10)
                .doesNotContainNull();
    }

    @Test
    void shouldBeReturnedFilm1And9() {
        Optional<Film> filmOptional = filmStorage.get(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );

        filmOptional = filmStorage.get(9);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 9L)
                                .hasFieldOrPropertyWithValue("name", "13-й воин")
                );
    }

    @Test
    void shouldBeReturnedEmptyForFilm99() {
        Optional<Film> filmOptional = filmStorage.get(99);

        assertThat(filmOptional)
                .isEmpty();
    }

    @Test
    void shouldBeCreateAndDeleteFilm11() {
        Optional<Film> filmOptional = filmStorage.create(correctFilm);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 11L)
                                .hasFieldOrPropertyWithValue("name", "Correct Movie")
                );

        assertThat(filmOptional.get().getMpa())
                .hasFieldOrPropertyWithValue("id", 1);

        filmStorage.remove(11L);

        Collection<Film> allFilms = filmStorage.findAll();
        assertThat(allFilms)
                .isNotEmpty()
                .hasSize(10);
    }

    @Test
    void shouldBeUpdateFilm3() {
        correctFilm.setId(3);
        correctFilm.setName("Обновленное название");
        Optional<Film> filmOptional = filmStorage.update(3, correctFilm);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", 3L)
                                .hasFieldOrPropertyWithValue("name", "Обновленное название")
                );
    }

    @Test
    void shouldBeSetLikeToFilm4() {
        Optional<Film> filmOptional = filmStorage.get(4);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 2L)
                );
        filmStorage.like(4, 1);

        Optional<Film> filmLikePlusOptional = filmStorage.get(4);
        assertThat(filmLikePlusOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 3L)
                );
    }

    @Test
    void shouldBeRemoveLikeFilm6() {
        Optional<Film> filmOptional = filmStorage.get(6);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 2L)
                );
        filmStorage.removeLike(6, 4);

        Optional<Film> filmLikePlusOptional = filmStorage.get(6);
        assertThat(filmLikePlusOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", 1L)
                );
    }
}