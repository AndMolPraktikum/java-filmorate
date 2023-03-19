package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Collection<Film> findAll();

    Film get(long key);

    Film create(Film film);

    Film update(long key, Film film);

    void remove(long key);

    boolean isContains(long key);
}
