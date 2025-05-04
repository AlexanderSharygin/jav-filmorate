package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {

    private final HashMap<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film add(Film film) {
        if (films.values().stream()
                .anyMatch(k -> k.getName().equals(film.getName()) &&
                        k.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new AlreadyExistException("Film with name " + film.getName() + " and release date " +
                    film.getReleaseDate() + " already exists");
        }
        idCounter++;
        film.setId(idCounter);
        film.setId(idCounter);
        films.put(idCounter, film);
        log.info("Add new film: {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        Optional<Film> existedFilm = Optional.ofNullable(films.get(film.getId()));
        if (existedFilm.isEmpty()) {
            log.info("Film warn id={} is not exist.", film.getId());
            throw new NotExistException("Film with id=" + film.getId() + " is not exist.");
        }
        existedFilm.get().setReleaseDate(film.getReleaseDate());
        existedFilm.get().setDescription(film.getDescription());
        existedFilm.get().setName(film.getName());
        existedFilm.get().setDuration(film.getDuration());
        log.info("Film is updated: {}", film.getName());

        return existedFilm.get();

    }

    @Override
    public boolean isExist(long id) {
        return films.containsKey(id);
    }
}
