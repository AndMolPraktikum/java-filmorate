package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class Mpa {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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