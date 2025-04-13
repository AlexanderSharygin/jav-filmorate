package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final Storage<User> userStorage;

    @Autowired
    public UserService(Storage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(long id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            throw new NotExistException("User with id=" + id + " is not exist.");
        } else {
            return user.get();
        }
    }

    public User addUser(User user) {
        checkName(user);

        return userStorage.add(user);
    }

    public User updateUser(User user) {
        checkName(user);

        return userStorage.update(user);
    }

    public void addFriend(long userId, long friendId) {
        Optional<User> user = userStorage.getById(userId);
        Optional<User> friend = this.userStorage.getById(friendId);
        isUsersExists(user, friend, userId, friendId);
        if (!user.get().getFriends().contains(friendId)) {
            user.get().getFriends().add(friendId);
            friend.get().getFriends().add(userId);
            log.info("User with id {} add User with id {} to friends list", userId, friendId);
        } else {
            throw new AlreadyExistException("User with id " + userId + " already friend for user " + friendId);
        }
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        Optional<User> user = userStorage.getById(userId);
        Optional<User> friend = this.userStorage.getById(friendId);
        isUsersExists(user, friend, userId, friendId);
        Set<Long> firstUserFriends = user.get().getFriends();
        Set<Long> secondUserFriends = friend.get().getFriends();
        Set<Long> commonFriendsId = firstUserFriends.stream()
                .filter(secondUserFriends::contains).collect(Collectors.toSet());

        List<User> commonFriendsList = new ArrayList<>();
        for (long id : commonFriendsId) {
            Optional<User> commonFriend = userStorage.getById(id);
            commonFriend.ifPresent(commonFriendsList::add);
        }

        return commonFriendsList;
    }

    public List<User> getUserFriends(long userId) {
        Optional<User> user = userStorage.getById(userId);
        if (user.isEmpty()) {
            throw new NotExistException("User with specified id " + userId + "is not exist");
        }
        Set<Long> friendsId = user.get().getFriends();
        List<User> friendsList = new ArrayList<>();
        for (long id : friendsId) {
            Optional<User> commonFriend = userStorage.getById(id);
            commonFriend.ifPresent(friendsList::add);
        }

        return friendsList;
    }


    public void removeFriend(long userId, long friendId) {
        Optional<User> user = userStorage.getById(userId);
        Optional<User> friend = this.userStorage.getById(friendId);
        isUsersExists(user, friend, userId, friendId);
        if (user.get().getFriends().contains(friendId)) {
            user.get().getFriends().remove(friendId);
            friend.get().getFriends().remove(userId);
            log.info("User with id {} removed user with id {} from friends list", userId, friendId);
        } else {
            throw new AlreadyExistException("User with id " + userId + " has not friend with user " + friendId);
        }
    }

    private void isUsersExists(Optional<User> user, Optional<User> friend, long userId, long friendId) {
        if (user.isEmpty()) {
            throw new NotExistException("User with specified id " + userId + "is not exist");
        }
        if (friend.isEmpty()) {
            throw new NotExistException("User with specified id " + friendId + "is not exist");
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Username is empty for user: {}. Email will be used as a login", user);
            user.setName(user.getLogin());
        }
    }
}