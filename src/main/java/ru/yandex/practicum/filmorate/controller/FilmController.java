package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Входящий запрос GET /films.");
        Collection<Film> allFilms = filmService.findAll();
        log.info("Исходящий ответ: {}", allFilms);
        return allFilms;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        log.info("Входящий запрос GET /films/{}.", id);
        Film filmById = filmService.get(id);
        log.info(" Исходящий ответ: {}", filmById);
        return filmById;
    }

    @GetMapping("/popular")
    public Collection<Film> findPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Входящий запрос GET /films/popular?count={}", count);
        Collection<Film> popularFilms = filmService.findPopular(count);
        log.info("Исходящий ответ: {}", popularFilms);
        return popularFilms;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Входящий запрос POST: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Исходящий ответ: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Входящий запрос PUT /films/{}/like/{}", id, userId);
        filmService.like(id, userId, "add");
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmValidationException {
        log.info("Входящий запрос PUT /films: {}", film);
        long key = film.getId();
        Film updatedFilm = filmService.update(key, film);
        log.info("Исходящий ответ: {}", updatedFilm);
        return updatedFilm;
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        log.info("Входящий запрос DELETE /films, c ID: {}", id);
        filmService.remove(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Входящий запрос DELETE /films/{}/like/{}", id, userId);
        filmService.like(id, userId, "remove");
    }
}

