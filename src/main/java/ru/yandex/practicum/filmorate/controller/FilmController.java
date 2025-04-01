package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private long idCounter = 1;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return films;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (films.stream()
                .anyMatch(k -> k.getName().equals(film.getName())
                        && k.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new AlreadyExistException("Film with name " + film.getName() + " and release date " +
                    film.getReleaseDate() + " already exists");
        }
        idCounter++;
        film.setId(idCounter);
        films.add(film);
        log.info("Add new film: {}", film);

        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        Optional<Film> existedFilm = films.stream()
                .filter(k -> k.getId().equals(film.getId()))
                .findFirst();
        if (existedFilm.isEmpty()) {
            log.info("Film warn id={} is not exist.", film.getId());
            throw new NotExistException("Film with id=" + film.getId() + " is not exist.");
        } else {
            existedFilm.get().setReleaseDate(film.getReleaseDate());
            existedFilm.get().setDescription(film.getDescription());
            existedFilm.get().setName(film.getName());
            existedFilm.get().setDuration(film.getDuration());
            log.info("Film is updated: {}", film.getName());

            return existedFilm.get();
        }
    }
}