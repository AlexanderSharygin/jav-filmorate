package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Long usersId) {
        return userService.getUserById(usersId);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        return friendService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        return friendService.removeFriend(userId, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        return friendService.getFriendsForUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long friendId) {
        return friendService.getCommonFriends(id, friendId);
    }
}