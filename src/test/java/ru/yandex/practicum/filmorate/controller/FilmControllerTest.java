package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(value = {"ru.yandex.practicum.filmorate"})
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Film correctFilm;
    private Film incorrectFilm;

    @BeforeEach
    public void init() {
        correctFilm = Film.builder()
                .name("Correct Movie")
                .description("A wonderful film about the life of programmers")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(100)
                .mpa(new Mpa(1))
                .build();

        incorrectFilm = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1900, 3, 25))
                .duration(200)
                .mpa(new Mpa(1))
                .build();
    }

    @AfterEach
    protected void clear() {
        correctFilm = null;
        incorrectFilm = null;
    }

    @Test
    void shouldBeReturnStatusOkAndId1() throws Exception {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(correctFilm))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldBeReturnStatus400() throws Exception {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(incorrectFilm))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldBeReturnStatus400WhenDescriptionMore200() throws Exception {
        Film incorrectFilm2 = correctFilm.toBuilder()
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. " +
                        "О. Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(incorrectFilm2))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldBeReturnStatus500WhenIncorrectReleaseDate() throws Exception {
        Film incorrectFilm3 = correctFilm.toBuilder()
                .releaseDate(LocalDate.of(1890, 2, 25))
                .build();

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(incorrectFilm3))
                        .contentType("application/json"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldBeReturnStatus400WhenNegativeDuration() throws Exception {
        Film incorrectFilm3 = correctFilm.toBuilder()
                .duration(-200)
                .build();

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(incorrectFilm3))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldBeReturnStatusOkWhenUpdate() throws Exception {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(correctFilm))
                .contentType("application/json"));

        final Film filmUpdate = correctFilm.toBuilder()
                .id(1)
                .name("Film Updated")
                .description("New film update decription")
                .releaseDate(LocalDate.of(1989, 4, 17))
                .duration(190)
                .build();

        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmUpdate))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Film Updated"))
                .andExpect(jsonPath("$.description").value("New film update decription"))
                .andExpect(jsonPath("$.duration").value("190"))
                .andExpect(jsonPath("$.releaseDate").value("1989-04-17"));
    }

    @Test
    void shouldBeReturnStatus500WhenUpdate() throws Exception {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(correctFilm))
                .contentType("application/json"));

        final Film filmUpdate = correctFilm.toBuilder()
                .id(999)
                .name("Film Updated")
                .description("New film update decription")
                .releaseDate(LocalDate.of(1989, 4, 17))
                .duration(190)
                .build();

        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(filmUpdate))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldBeReturnStatusOkWhenGetAll() throws Exception {
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(correctFilm))
                .contentType("application/json"));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void shouldReturnStatus404() throws Exception {
        final User friend = User.builder()
                .login("friend2")
                .name("")
                .email("friendmail2@yandex.ru")
                .birthday(LocalDate.of(2000, 9, 23))
                .build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(friend))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(correctFilm))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/films/1/like/8"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}
