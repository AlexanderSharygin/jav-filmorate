package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public class FilmRepository extends BaseRepository {

    private static final String SQL_GET_FILM = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS RATE_ID, r.name AS RATE_NAME " +
            "FROM films f LEFT JOIN rates r on f.rate_id = r.id WHERE f.id = ?";
    private static final String SQL_GET_NEWEST_FILM = "SELECT TOP 1 F.ID AS film_id, F.NAME, F.DESCRIPTION ,F.RELEASE_DATE, " +
            "F.DURATION, F.RATE_ID AS RATE_ID, R.NAME AS RATE_NAME " +
            "from FILMS F LEFT JOIN RATES R on F.RATE_ID = R.ID ORDER BY F.ID DESC";
    private static final String SQL_GET_FILMS = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS RATE_ID, r.name AS RATE_NAME " +
            "FROM films f LEFT JOIN rates r ON f.rate_id = r.id";
    private static final String SQL_INSERT_FILM = "INSERT INTO films (name, description, release_date, duration, rate_id) " +
            "VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FILM = "UPDATE FILMS " +
            "SET name=?, description=?, release_date=?, duration=?, rate_id=? WHERE id=?";
    private static final String SQL_GET_POPULAR_FILMS = "SELECT f.ID AS film_id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
            "f.DURATION, f.RATE_ID, m.NAME AS RATE_NAME FROM FILMS f " +
            "LEFT JOIN RATES m ON f.RATE_ID = m.ID " +
            "LEFT JOIN (SELECT FILM_ID, COUNT(FILM_ID) AS likes_count FROM LIKES GROUP BY FILM_ID) l ON " +
            "f.ID = l.FILM_ID ORDER BY COALESCE(l.likes_count, 0) DESC " +
            "LIMIT ?";

    @Autowired
    public FilmRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper, Film.class);
    }

    public Optional<Film> findById(Long filmId) {
        return findOne(SQL_GET_FILM, filmId);
    }

    public Optional<Film> findNew() {
        return findOne(SQL_GET_NEWEST_FILM);
    }

    public List<Film> findAll() {
        return findMany(SQL_GET_FILMS);
    }

    public void add(Film value) {
        insert(SQL_INSERT_FILM, value.getName(), value.getDescription(), value.getReleaseDate(),
                value.getDuration(), value.getMpa().getId());
    }

    public void update(Film film) {
        update(SQL_UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
    }

    public List<Film> findPopulars(int count) {
        return findMany(SQL_GET_POPULAR_FILMS, count);
    }

}