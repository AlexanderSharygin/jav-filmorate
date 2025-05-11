package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreFilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final GenreFilmRepository genreFilmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, GenreRepository genreRepository, GenreFilmRepository
            genreFilmRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.genreFilmRepository = genreFilmRepository;
    }

    public Film getFilmById(long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new NotExistException("Film with id " + id + " not exists in the DB"));
        film.setGenres(genreRepository.findByFilmId(film.getId()));

        return film;
    }


    public List<Film> getAll() {
        List<Film> films = filmRepository.findAll();
        films.forEach(k -> k.setGenres(genreRepository.findByFilmId(k.getId())));

        return films;
    }

    public Film addFilm(Film film) {
        Film createdFilm;
        try {
            filmRepository.add(film);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Mpa rate with specified id not exists in the DB");
        }
        createdFilm = filmRepository.findNew().orElseThrow(
                () -> new AlreadyExistException("Film already exists in the DB"));
        addGenresForFilm(film, createdFilm.getId());
        createdFilm.setGenres(genreRepository.findByFilmId(createdFilm.getId()));

        return createdFilm;
    }

    public Film updateFilm(Film film) {
        try {
            genreFilmRepository.removeForFilm(film.getId());
            filmRepository.update(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film.getId() + " not exists in the DB");
        }
        Film updatedFilm = getFilmById(film.getId());
        addGenresForFilm(film, updatedFilm.getId());
        updatedFilm.setGenres(genreRepository.findByFilmId(film.getId()));

        return updatedFilm;
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> films = filmRepository.findPopulars(count);
        films.forEach(k -> k.setGenres(genreRepository.findByFilmId(k.getId())));

        return films;
    }

    private void addGenresForFilm(Film oldFilm, Long newFilmId) {
        Set<Long> genreIds = new HashSet<>((oldFilm.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList())));
        for (var genre : genreIds) {
            try {
                genreFilmRepository.addForFilm(newFilmId, genre);
            } catch (DuplicateKeyException e) {
                throw new AlreadyExistException("Film with genre already exists in the DB");
            }
        }
    }
}