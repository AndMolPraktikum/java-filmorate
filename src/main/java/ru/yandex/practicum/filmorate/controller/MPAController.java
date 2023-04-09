package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MPAController {

    private final MpaService mpaService;

    @Autowired
    public MPAController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Входящий запрос GET /mpa.");
        Collection<Mpa> allMpas = mpaService.findAll();
        log.info("Исходящий ответ: {}", allMpas);
        return allMpas;
    }

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable Integer id) {
        log.info("Входящий запрос GET /mpa/{}.", id);
        Mpa mpaById = mpaService.get(id);
        log.info(" Исходящий ответ: {}", mpaById);
        return mpaById;
    }
}
