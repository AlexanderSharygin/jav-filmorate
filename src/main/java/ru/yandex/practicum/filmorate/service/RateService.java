package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RateRepository;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.List;

@Service
@Slf4j
public class RateService {
    private final RateRepository rateRepository;

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public Rate getRateById(long id) {
        return rateRepository
                .findById(id)
                .orElseThrow(() -> new NotExistException("MPA with id " + id + " not exists in the DB"));
    }

    public List<Rate> getRates() {
        return rateRepository.findAll();
    }
}