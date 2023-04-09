package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAll();

    Optional<Film> get(long key);

    Optional<Film> create(Film film);

    Optional<Film> update(long key, Film film);

    void remove(long key);

    void like(long filmId, long userId);

    boolean likeCheck(long filmId, long userId);

    void removeLike(long filmId, long userId);

}
