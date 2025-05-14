package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


@Service
@Slf4j
public class LikeService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(FilmRepository filmRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public boolean addLike(long filmId, long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotExistException("Films are not exist in the DB"));
        Film film = filmRepository
                .findById(filmId)
                .orElseThrow(() -> new NotExistException("Films are not exist in the DB"));
        try {
            likeRepository.add(film.getId(), user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already like film with id " + filmId);
        }
        return true;
    }

    public boolean removeLike(long filmId, long userId) {
        likeRepository.find(filmId, userId)
                .orElseThrow(() -> new NotExistException("User with id " + userId + " is not like film with id " +
                        filmId));
        likeRepository.remove(filmId, userId);
        return true;
    }
}