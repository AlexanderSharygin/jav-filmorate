package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements Storage<User> {
    private final HashMap<Long, User> users = new HashMap<>();
    private long idCounter = 0;

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getById(long id) {
       return Optional.ofNullable(users.get(id));

    }

    @Override
    public User add(User user) {
        if (users.values().stream().anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("User account with email " + user.getEmail() + " already exists.");
        }
        idCounter++;
        user.setId(idCounter);
        users.put(idCounter, user);
        log.info("User is added: {}", user);

        return user;
    }




    @Override
    public User update(User user) {
        Optional<User> existedUser = users.values().stream()
                .filter(k -> k.getId().equals(user.getId()))
                .findFirst();
        if (existedUser.isEmpty()) {
            throw new NotExistException("User with id=" + user.getId() + " is not exist.");
        }
        existedUser.get().setEmail(user.getEmail());
        existedUser.get().setLogin(user.getLogin());
        existedUser.get().setName(user.getName());
        existedUser.get().setBirthday(user.getBirthday());
        log.info("User updated: {}", user);

        return existedUser.get();
    }

    @Override
    public boolean isExist(long id) {
        return users.containsKey(id);
    }


}
