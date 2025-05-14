package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getLong("USER_ID"),
                resultSet.getString("NAME"),
                resultSet.getString("LOGIN"),
                resultSet.getString("EMAIL"),
                resultSet.getDate("BIRTHDAY").toLocalDate());
    }
}
