package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.Status;

@Data
@Builder(toBuilder = true)
public class Friend {

    private long id;
    private Status status;

    public Friend(long id, Status status) {
        this.id = id;
        this.status = status;
    }
}
