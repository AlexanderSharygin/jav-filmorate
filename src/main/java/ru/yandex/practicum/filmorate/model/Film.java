package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.serializer.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serializer.LocalDateSerializer;
import ru.yandex.practicum.filmorate.validator.MinDateValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @MinDateValue(minDate = "1895-12-28")
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
    private Set<Long> likeUsers;
    private List<Genre> genres;
    private Rate mpa;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeUsers = new HashSet<>();
    }

    public Film() {
        this.genres = new ArrayList<>();
        mpa = new Rate();
    }
}
