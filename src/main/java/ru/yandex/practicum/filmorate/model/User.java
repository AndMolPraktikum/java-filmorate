package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class User {

    private long id;
    @NonNull
    @Email(message = "Некорректный адрес электронной почты")
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    private List<Long> friendIds;

    public User(long id, @NonNull String email, String login, String name, LocalDate birthday, List<Long> friendIds) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendIds = friendIds;
    }

    public User(@NonNull String email, String login, String name, LocalDate birthday, List<Long> friendIds) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendIds = friendIds;
    }

    public User(@NonNull String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
    }
}
