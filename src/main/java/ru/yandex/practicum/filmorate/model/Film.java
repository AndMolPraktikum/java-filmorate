package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long likes;
    @JsonIgnore
    private Set<Long> userIds;
    private Set<Genre> genres;
    @NotNull
    private Mpa mpa;  //возрастное ограничение

    public Film(long id, String name, String description, LocalDate releaseDate, long duration, long likes,
                Set<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Film(long id, String name, String description, LocalDate releaseDate, long duration, int rate, long likes,
                Set<Long> userIds, Set<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.likes = likes;
        this.userIds = userIds;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Film(String name, String description, LocalDate releaseDate, long duration, int rate,
                Set<Genre> genres, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Film() {
    }
}
