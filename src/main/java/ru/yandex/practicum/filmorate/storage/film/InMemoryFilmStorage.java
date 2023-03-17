package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private long id = 0;
    private final Map<Long, Film> films = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilmById(long key) {
        if (films.containsKey(key)) {
            return films.get(key);
        } else {
            log.error("Фильм с таким ID не существует: {}", key);
            throw new FilmNotFoundException(String.format("Фильм с ID %d не существует", key));
        }
    }

    @Override
    public Film create(Film film) {
        id++;
        film.setId(id);
        film.setWhoLiked(new HashSet<>());
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(long key, Film film) {
        if (film.getWhoLiked() == null) {
            film.setWhoLiked(new HashSet<>());
        }
        films.put(key, film);
        return films.get(key);
    }

    @Override
    public boolean isContains(long key) {
        return films.containsKey(key);
    }

    @Override
    public void remove(long key) {
        films.remove(key);
    }
}
