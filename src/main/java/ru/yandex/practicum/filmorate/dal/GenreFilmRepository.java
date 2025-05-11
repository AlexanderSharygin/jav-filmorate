package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

@Component
public class GenreFilmRepository extends BaseRepository {

    private static final String SQL_INSERT_GENRE = "INSERT INTO GENRES_FILMS (genre_id, films_id) VALUES (?,?)";
    private static final String SQL_DELETE_GENRES_FOR_FILM = "DELETE FROM genres_films WHERE films_id=?";

    public GenreFilmRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, null, FilmGenre.class);
    }

    public void addForFilm(Long filmId, Long genreId) {
        update(SQL_INSERT_GENRE, genreId, filmId);
    }

    public void removeForFilm(Long filmId) {
        remove(SQL_DELETE_GENRES_FOR_FILM, filmId);
    }
}