package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Genre {

    @NonNull
    Long id;

    @NonNull
    String name;

    public Genre() {
    }
}