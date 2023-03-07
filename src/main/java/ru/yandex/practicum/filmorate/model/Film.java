package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {

    private int id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
    //private int rate;  //в ТЗ не указано, но в тестах присутствует
}
