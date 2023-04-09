package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<Mpa> findAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa get(int id) {
        Optional<Mpa> mpaOptional = mpaDbStorage.get(id);
        if (mpaOptional.isEmpty()) {
            log.error("MPA с таким ID не существует: {}", id);
            throw new MpaNotFoundException(String.format("MPA с ID %d не существует", id));
        }
        return mpaOptional.get();
    }
}
