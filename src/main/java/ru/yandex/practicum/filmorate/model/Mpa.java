package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Mpa {

    private Integer id;

    private String name;

    public Mpa(Integer id) {
        this.id = id;
    }

    public Mpa(String name) {
        this.name = name;
    }

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa() {
    }
}
