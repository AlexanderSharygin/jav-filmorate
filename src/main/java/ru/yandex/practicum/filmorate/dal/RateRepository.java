package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.List;
import java.util.Optional;

@Component
public class RateRepository extends BaseRepository {


    private static final String SQL_GET_MPA = "SELECT id, name FROM rates WHERE id=?";

    private static final String SQL_GET_MPAs = "SELECT id, name FROM RATES";

    @Autowired
    public RateRepository(JdbcTemplate jdbcTemplate, RowMapper<Rate> mapper) {
        super(jdbcTemplate, mapper, Rate.class);
    }


    public Optional<Rate> findById(Long rateId) {
        return findOne(SQL_GET_MPA, rateId);
    }


    public List<Rate> findAll() {
        return findMany(SQL_GET_MPAs);
    }
}