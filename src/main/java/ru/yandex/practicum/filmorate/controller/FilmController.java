package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Входящий запрос GET /films. Исходящий ответ: {}", films.values());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        isValid(film);
        log.info("Входящий запрос POST: {}", film);
        id++;
        film.setId(id);
        films.put(id, film);
        log.info("Исходящий ответ: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmValidationException {
        isValid(film);
        log.info("Входящий запрос PUT: {}", film);
        Integer key = film.getId();
        if (films.containsKey(key)) {
            films.put(key, film);
            log.info("Исходящий ответ: {}", film);
            return films.get(key);
        } else {
            log.error("Фильм с таким ID не существует: {}", film.getId());
            throw new FilmValidationException("Фильм с таким ID не существует");
        }
    }

    private void isValid(Film film) throws FilmValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше дня рождения кино: {}", film.getReleaseDate());
            throw new FilmValidationException("В то время фильмы не снимались");
        }
    }
}

