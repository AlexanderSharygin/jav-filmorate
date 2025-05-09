package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Rate {

    @NonNull
    Long id;

    @NonNull
    String name;

    public Rate() {
    }
}