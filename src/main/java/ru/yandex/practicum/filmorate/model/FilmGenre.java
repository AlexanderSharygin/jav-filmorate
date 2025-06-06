package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilmGenre {

    @NotNull
    private Integer filmId;
    @NotNull
    private Integer genreId;

    public FilmGenre() {
    }
}
