package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Friend {

    @NotNull
    Long userId;

    @NotNull
    Long friendId;


    public Friend() {
    }
}