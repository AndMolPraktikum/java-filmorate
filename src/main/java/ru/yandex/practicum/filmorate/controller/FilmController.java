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
        log.info("Входящий запрос GET /films. Исходящий ответ: {}", filmService.findAll());
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        log.info("Входящий запрос GET /films/{}. Исходящий ответ: {}", id, filmService.findFilmById(id));
        return filmService.findFilmById(id);
    }

    //GET "/popular?count={count}" возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public Collection<Film> findPopular(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Входящий запрос GET /films/popular?count={}. Исходящий ответ: {}", count,
                filmService.findPopular(count));
        return filmService.findPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Film responseFilm = filmService.create(film);
        log.info("Входящий запрос POST: {}, Исходящий ответ: {}", film, responseFilm);
        return responseFilm;
    }

    //PUT "/{id}/like/{userId}"  пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable Long id, @PathVariable Long userId) {
        filmService.like(id, userId);
        log.info("Входящий запрос PUT /films/{}/like/{}", id, userId);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmValidationException {
        long key = film.getId();
        Film responseFilm = filmService.update(key, film);
        log.info("Входящий запрос PUT /films: {}, Исходящий ответ: {}", film, responseFilm);
        return responseFilm;
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        filmService.remove(id);
        log.info("Входящий запрос DELETE /films, c ID: {}", id);
    }

    //DELETE "/{id}/like/{userId}" пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.info("Входящий запрос DELETE /films/{}/like/{}", id, userId);
    }
}

