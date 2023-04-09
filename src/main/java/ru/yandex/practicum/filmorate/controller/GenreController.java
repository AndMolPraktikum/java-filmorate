package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAll() {
        log.info("Входящий запрос GET /genres.");
        Collection<Genre> allGenres = genreService.findAll();
        log.info("Исходящий ответ: {}", allGenres);
        return allGenres;
    }

    @GetMapping("/{id}")
    public Genre findMpaById(@PathVariable Integer id) {
        log.info("Входящий запрос GET /genres/{}.", id);
        Genre genreById = genreService.get(id);
        log.info(" Исходящий ответ: {}", genreById);
        return genreById;
    }
}
