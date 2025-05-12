package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreFilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RateRepository;
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
    private final RateRepository rateRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, GenreRepository genreRepository, GenreFilmRepository
            genreFilmRepository, RateRepository rateRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.genreFilmRepository = genreFilmRepository;
        this.rateRepository = rateRepository;
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
        rateRepository.findById(film.getMpa().getId())
                .orElseThrow(() -> new NotExistException("Rate with id " + film.getMpa().getId() +
                        " not exists in the DB"));
        List<Long> genreIds = film.getGenres().stream().map(Genre::getId).toList();
        genreIds.forEach(k -> genreRepository.findById(k)
                .orElseThrow(() -> new NotExistException("Genre with id " + k + " not exists in the DB")));

        filmRepository.add(film);
        createdFilm = filmRepository.findNew().orElseThrow(
                () -> new AlreadyExistException("Film already exists in the DB"));
        addGenresForFilm(film, createdFilm.getId());
        createdFilm.setGenres(genreRepository.findByFilmId(createdFilm.getId()));

        return createdFilm;
    }

    public Film updateFilm(Film film) {
        filmRepository.findById(film.getId())
                .orElseThrow(() -> new NotExistException("User with id " + film.getId() + " not exists in the DB"));
        filmRepository.update(film);
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