package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotLikedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film findFilmById(long key) {
        return inMemoryFilmStorage.findFilmById(key);
    }

    //вывод 10 наиболее популярных фильмов по количеству лайков
    public Collection<Film> findPopular(int count) {
        return findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        isValid(film);
        return inMemoryFilmStorage.create(film);
    }

    public Film update(long key, Film film) throws FilmValidationException {
        isValid(film);
        if (inMemoryFilmStorage.isContains(key)) {
            return inMemoryFilmStorage.update(key, film);
        } else {
            log.error("Фильм с таким ID не существует: {}", film.getId());
            throw new FilmValidationException("Фильм с таким ID не существует");
        }
    }

    //добавление лайка
    public void like(Long filmId, Long userId) {
        if (inMemoryUserStorage.isContains(userId)) {
            inMemoryFilmStorage.getFilms().get(filmId).getWhoLiked().add(userId);
            inMemoryFilmStorage.getFilms().get(filmId).setRate(
                    inMemoryFilmStorage.getFilms().get(filmId).getRate() + 1);
        } else {
            log.error("Пользователя с ID {} не существует", userId);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", userId));
        }
    }

    //удаление лайка
    public void removeLike(Long filmId, Long userId) {
        if (inMemoryFilmStorage.getFilms().get(filmId).getWhoLiked().contains(userId)) {
            inMemoryFilmStorage.getFilms().get(filmId).getWhoLiked().remove(userId);
            inMemoryFilmStorage.getFilms().get(filmId).setRate(
                    inMemoryFilmStorage.getFilms().get(filmId).getRate() - 1);
        } else {
            log.error("Пользователь с ID {} не отмечал этот фильм", userId);
            throw new UserNotLikedException(String.format("Пользователь с ID %d не отмечал этот фильм", userId));
        }
    }

    public void remove(long id) {
        inMemoryFilmStorage.remove(id);
    }

    private void isValid(Film film) throws FilmValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше дня рождения кино: {}", film.getReleaseDate());
            throw new FilmValidationException("В то время фильмы не снимались");
        }
    }

    private int compare(Film f0, Film f1) {
        return (f0.getRate() - f1.getRate()) * -1;
    }
}
