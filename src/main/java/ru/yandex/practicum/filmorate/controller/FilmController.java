package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    private final LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") long filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public boolean addLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return likeService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") long userId) {
        return likeService.removeLike(filmId, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {
            throw new BadRequestException("count");
        }

        return filmService.getMostPopularFilms(count);
    }
}