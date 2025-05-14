package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public FriendService(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public boolean addFriend(long userId, long friendId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        User friend = userRepository
                .findById(friendId)
                .orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        try {
            friendRepository.add(user.getId(), friend.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already friend with user with id "
                    + friendId);
        }

        return true;
    }

    public boolean removeFriend(long userId, long friendId) {
        userRepository.findById(userId).orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        userRepository.findById(friendId).orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        try {
            Friend friend = friendRepository.find(userId, friendId).get();
            friendRepository.remove(friend.getUserId(), friend.getFriendId());
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<User> getFriendsForUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        return userRepository.findFriends(userId);
    }


    public List<User> getCommonFriends(long id, long friendId) {
        return userRepository.findCommonFriends(id, friendId);
    }
}