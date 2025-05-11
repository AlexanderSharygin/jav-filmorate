package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository extends BaseRepository<User> {

    private static final String SQL_GET_USER = "SELECT * FROM users WHERE user_id = ?";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_GET_NEWEST_USER = "SELECT TOP 1 * FROM users ORDER BY user_id DESC";
    private static final String SQL_ADD_USER = "INSERT INTO users (name, email, birthday, login) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE users SET name =?, email=?, birthday=?, login=? " +
            "WHERE user_id=?";
    private static final String SQL_GET_FRIENDS_FOR_USER = "SELECT * FROM users WHERE user_id IN" +
            " (SELECT friend_id FROM users_users WHERE users_id=?)";
    private static final String SQL_GET_COMMON_FRIENDS = "SELECT * FROM users u WHERE user_id IN " +
            "(SELECT friend_id  FROM users_users WHERE users_id = ? OR users_id  = ? " +
            "GROUP BY friend_id HAVING COUNT (friend_id) = 2)";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public Optional<User> findById(long userId) {
        return findOne(SQL_GET_USER, userId);
    }

    public Optional<User> findNew() {
        return findOne(SQL_GET_NEWEST_USER);
    }

    public List<User> findAll() {
        return findMany(SQL_GET_ALL_USERS);
    }

    public User add(User user) {
        long id = insert(SQL_ADD_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(SQL_UPDATE_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin(), user.getId());
        return user;
    }

    public List<User> findFriends(Long userId) {
        return findMany(SQL_GET_FRIENDS_FOR_USER, userId);
    }

    public List<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        return findMany(SQL_GET_COMMON_FRIENDS, firstUserId, secondUserId);
    }
}