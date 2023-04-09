package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Genre {

    private Integer id;
    private String name;

    public Genre(Integer id) {
        this.id = id;
    }

    public Genre(String name) {
        this.name = name;
    }

    public Genre() {
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
