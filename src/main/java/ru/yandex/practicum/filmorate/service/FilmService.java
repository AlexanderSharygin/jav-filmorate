package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {

    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> filmStorage, Storage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(long id) {
        Optional<Film> film = filmStorage.getById(id);
        if (film.isEmpty()) {
            log.info("Film warn id={} is not exist.", id);
            throw new NotExistException("Film with id=" + id + " is not exist.");
        } else {
            log.info("Found film with с id: {}", film.get().getId());
            return film.get();
        }
    }

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> f2.getLikeUsers().size() - f1.getLikeUsers().size())
                .limit(count).toList();
    }

    public void addLike(long filmId, long userId) {
        Optional<Film> film = filmStorage.getById(filmId);
        Optional<User> user = userStorage.getById(userId);
        checkFilmAndUserExists(film, user, filmId, userId);
        if (!film.get().getLikeUsers().contains(userId)) {
            film.get().getLikeUsers().add(userId);
            log.info("User with id {} liked film with id {}", userId, filmId);
        } else {
            throw new AlreadyExistException("User with id " + userId + " already like film with id " + filmId);
        }

    }

    public void removeLike(long filmId, long userId) {
        Optional<Film> film = filmStorage.getById(filmId);
        Optional<User> user = userStorage.getById(userId);
        checkFilmAndUserExists(film, user, filmId, userId);
        if (film.get().getLikeUsers().contains(userId)) {
            film.get().getLikeUsers().remove(userId);
            log.info("User with id {} removed like for film with id {}", userId, filmId);

        } else {
            throw new AlreadyExistException("User with id " + userId + " not like film with id " + filmId);
        }
    }

    private void checkFilmAndUserExists(Optional<Film> film, Optional<User> user, long filmId, long userId) {
        if (film.isEmpty()) {
            throw new NotExistException("Film with id=" + filmId + " is not exist.");
        }
        if (user.isEmpty()) {
            throw new NotExistException("User with specified id " + userId + "is not exist");
        }
    }
}