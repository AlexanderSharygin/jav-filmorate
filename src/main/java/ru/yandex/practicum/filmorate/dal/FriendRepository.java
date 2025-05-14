package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Optional;

@Component
public class FriendRepository extends BaseRepository {

    private static final String SQL_INSERT_FRIEND = "INSERT INTO users_users(user_id, friend_id) " +
            "VALUES (?,?)";
    private static final String SQL_GET_FRIEND = "SELECT * FROM users_users WHERE user_id=? AND friend_id=?";
    private static final String SQL_REMOVE_FRIEND = "DELETE FROM users_users WHERE user_id=? AND friend_id=?";

    @Autowired
    public FriendRepository(JdbcTemplate jdbcTemplate, RowMapper<Friend> mapper) {
        super(jdbcTemplate, mapper, Friend.class);
    }

    public void add(Long userId, Long friendId) {
        insert(SQL_INSERT_FRIEND, userId, friendId);
    }

    public Optional<Friend> find(Long userId, Long friendId) {
        return findOne(SQL_GET_FRIEND, userId, friendId);
    }

    public void remove(Long userId, Long friendId) {
        remove(SQL_REMOVE_FRIEND, userId, friendId);
    }
}