package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
public class GenreRepository extends BaseRepository {

    private static final String SQL_GET_GENRES_FOR_FILM = "SELECT g.genre_id, g.name " +
            "FROM genres g JOIN genres_films gf ON g.genre_id=gf.genre_id WHERE gf.films_id = ?";
    private static final String SQL_GET_GENRES = "SELECT genre_id, name FROM genres";
    private static final String SQL_GET_GENRE = "SELECT genre_id, name FROM genres WHERE genre_id=? ";


    public GenreRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper, Genre.class);
    }

    public Optional<Genre> findById(Long genreId) {
        return findOne(SQL_GET_GENRE, genreId);
    }

    public List<Genre> findByFilmId(Long filmId) {
        return findMany(SQL_GET_GENRES_FOR_FILM, filmId);
    }


    public List<Genre> findAll() {
        return findMany(SQL_GET_GENRES);
    }
}
