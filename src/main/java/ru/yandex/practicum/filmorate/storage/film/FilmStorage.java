package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film get(long key);

    Film create(Film film);

    Film update(long key, Film film);

    void remove(long key);

    void like(long filmId, long userId);

    boolean likeCheck(long filmId, long userId);

    void removeLike(long filmId, long userId);

}
