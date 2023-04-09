package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Collection<Genre> findAll() {
        return genreDbStorage.findAll();
    }

    public Genre get(int id) {
        Optional<Genre> genreOptional = genreDbStorage.get(id);
        if (genreOptional.isEmpty()) {
            log.error("Жанр с таким ID не существует: {}", id);
            throw new GenreNotFoundException(String.format("Жанр с ID %d не существует", id));
        }
        return genreOptional.get();
    }
}
