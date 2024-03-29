package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotLikedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmDbStorage filmDbStorage;

    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public Collection<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public Film get(long key) {
        return filmDbStorage.get(key);
    }

    public Collection<Film> findPopular(int count) { //вывод 10 наиболее популярных фильмов по количеству лайков
        return findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        isValid(film);
        return filmDbStorage.create(film);
    }

    public Film update(long key, Film film) throws FilmValidationException {
        isValid(film);
        filmDbStorage.get(key);  //если фильма нет, то выбросится ошибка
        return filmDbStorage.update(key, film);
    }

    public void like(long filmId, long userId, String action) {
        userDbStorage.get(userId); //если юзера нет, то выбросится ошибка
        filmDbStorage.get(filmId); //если фильма нет, то выбросится ошибка

        if (action.equals("add")) {
            filmDbStorage.like(filmId, userId);
        }
        if (action.equals("remove")) {
            if (!filmDbStorage.likeCheck(filmId, userId)) {
                log.error("Пользователь с ID {} не отмечал этот фильм", userId);
                throw new UserNotLikedException(String.format("Пользователь с ID %d не отмечал этот фильм", userId));
            }
            filmDbStorage.removeLike(filmId, userId);
        }
    }

    public void remove(long id) {
        filmDbStorage.remove(id);
    }

    private void isValid(Film film) throws FilmValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше дня рождения кино: {}", film.getReleaseDate());
            throw new FilmValidationException("В то время фильмы не снимались");
        }
    }

    private int compare(Film f0, Film f1) {
        return (int) ((f0.getLikes() - f1.getLikes()) * -1);
    }
}
