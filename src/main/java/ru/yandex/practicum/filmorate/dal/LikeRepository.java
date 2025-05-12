package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Optional;

@Component
public class LikeRepository extends BaseRepository {

    private static final String SQL_INSERT_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";

    private static final String SQL_GET_LIKE = "SELECT * FROM likes WHERE user_id=? AND film_id=?";

    private static final String SQL_DELETE_LIKE = "DELETE FROM likes WHERE user_id=? AND film_id=?";


    @Autowired
    public LikeRepository(JdbcTemplate jdbcTemplate, RowMapper<Like> mapper) {
        super(jdbcTemplate, mapper, Like.class);
    }

    public Optional<Like> find(Long filmId, Long userId) {
        return findOne(SQL_GET_LIKE, userId, filmId);
    }

    public void add(Long filmId, Long userId) {
        insert(SQL_INSERT_LIKE, userId, filmId);
    }

    public void remove(Long filmId, Long userId) {
        remove(SQL_DELETE_LIKE, userId, filmId);
    }
}