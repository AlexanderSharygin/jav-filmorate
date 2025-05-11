package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NoContentException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotExistException("User with id " + id + " not exists in the DB"));
    }

    public User addUser(User user) {
        checkName(user);
        try {
            userRepository.add(user);
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistException("User already exists in the DB");
        }
        return userRepository.findNew()
                .orElseThrow(() -> new BadRequestException("Something went wrong."));
    }

    public User updateUser(User user) {
        checkName(user);
        try {
            userRepository.update(user);
        } catch (
                EmptyResultDataAccessException e) {
            throw new NotExistException("User not exists in the DB");
        }
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new NotExistException("User with id" + user.getId() + " not exists in the DB"));
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Username is empty for user: {}. Email will be used as a login", user);
            user.setName(user.getLogin());
        }
    }
}