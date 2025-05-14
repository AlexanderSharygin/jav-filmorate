package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;

@RestController
public class MpaController {
    private final RateService rateService;

    @Autowired
    public MpaController(RateService rateService) {

        this.rateService = rateService;
    }

    @GetMapping("/mpa/{id}")
    public Rate getMPAById(@PathVariable("id") long mpaId) {
        return rateService.getRateById(mpaId);
    }

    @GetMapping("/mpa")
    public List<Rate> getMPAs() {
        return rateService.getRates();
    }
}