package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {

    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
    private int rate;
    private Set<Long> whoLiked;//в ТЗ не указано, но в тестах присутствует
}
