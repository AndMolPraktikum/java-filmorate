package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreServiceTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    void findAll() {
        Collection<Genre> allGenres = genreDbStorage.findAll();

        assertThat(allGenres)
                .isNotEmpty()
                .hasSize(6)
                .doesNotContainNull()
                .extracting("name")
                .contains("Боевик", "Документальный", "Драма", "Комедия", "Мультфильм", "Триллер")
                ;

    }

    @Test
    void get() {
        Optional<Genre> genreOptional = genreDbStorage.get(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "Драма")
                );
    }
}