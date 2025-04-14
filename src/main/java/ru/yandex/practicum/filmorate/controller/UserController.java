package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private long idCounter = 0;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        if (users.stream().anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("User account with email " + user.getEmail() + " already exists.");
        }
        checkName(user);
        idCounter++;
        user.setId(idCounter);
        users.add(user);
        log.info("User is added: {}", user);

        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        Optional<User> existedUser = users.stream()
                .filter(k -> k.getId().equals(user.getId()))
                .findFirst();
        if (existedUser.isEmpty()) {
            throw new NotExistException("User with id=" + user.getId() + " is not exist.");
        } else {
            checkName(user);
            existedUser.get().setEmail(user.getEmail());
            existedUser.get().setLogin(user.getLogin());
            existedUser.get().setName(user.getName());
            existedUser.get().setBirthday(user.getBirthday());
            log.info("User updated: {}", user);

            return existedUser.get();
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Username is empty for user: {}. Email will be used as a login/", user);
            user.setName(user.getLogin());
        }
    }
}