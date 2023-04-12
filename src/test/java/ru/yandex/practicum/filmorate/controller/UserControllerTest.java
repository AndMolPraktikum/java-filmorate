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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(value = {"ru.yandex.practicum.filmorate"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User correctUser;
    private User incorrectUser;

    @BeforeEach
    protected void init() {
        correctUser = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1946, 8, 20))
                .friendIds(new ArrayList<>())
                .build();

        incorrectUser = User.builder()
                .email("yandex@mail.ru")
                .login("dolore ullamco")
                .birthday(LocalDate.of(2446, 8, 20))
                .friendIds(new ArrayList<>())
                .build();
    }

    @AfterEach
    protected void clear() {
        correctUser = null;
        incorrectUser = null;
    }

    @Test
    void shouldBeReturnStatusOkAndId1() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(correctUser))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Nick Name"));
    }

    @Test
    void shouldBeReturnStatus400WhiteSpace() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(incorrectUser))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeReturnStatus400IncorrectEmail() throws Exception {
        final User incorrectUser2 = correctUser.toBuilder()
                .email("yandexmail.ru")
                .build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(incorrectUser2))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeReturnStatus400IncorrectBirthday() throws Exception {
        final User incorrectUser3 = incorrectUser.toBuilder()
                .login("dolore42")
                .build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(incorrectUser3))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeReturnStatusOkWhenUpdate() throws Exception {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(correctUser))
                .contentType("application/json"));

        final User userUpdate = correctUser.toBuilder()
                .id(1)
                .login("doloreUpdate")
                .name("est adipisicing")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1973, 9, 23))
                .build();

        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userUpdate))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.login").value("doloreUpdate"))
                .andExpect(jsonPath("$.name").value("est adipisicing"))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.birthday").value("1973-09-23"));
    }

    @Test
    void shouldBeReturnStatus404WhenUpdate() throws Exception {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(correctUser))
                .contentType("application/json"));

        final User userIncorrectUpdate = correctUser.toBuilder()
                .id(9999)
                .login("doloreUpdate")
                .name("est adipisicing")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1973, 9, 23))
                .build();

        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(userIncorrectUpdate))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatusOkWhenGetAll() throws Exception {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(correctUser))
                .contentType("application/json"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void shouldReturnAssignedName() throws Exception {
        final User correctUser2 = correctUser.toBuilder()
                .name(null)
                .email("dolore@yandex.ru")
                .build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(correctUser2))
                        .contentType("application/json"))
                .andExpect(jsonPath("$.name").value("dolore"));
    }
}
