package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    void findAll() {
        Collection<Mpa> allMpas = mpaDbStorage.findAll();

        assertThat(allMpas)
                .isNotEmpty()
                .hasSize(5)
                .doesNotContainNull()
                .extracting("name")
                .contains("G", "NC-17", "PG", "PG-13", "R")
                .doesNotContain("6+");
    }

    @Test
    void get() {
        Optional<Mpa> mpaOptional = mpaDbStorage.get(5);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa)
                                .hasFieldOrPropertyWithValue("id", 5)
                                .hasFieldOrPropertyWithValue("name", "NC-17")
                );
    }
}