package com.service;

import com.dao.UserDao;
import com.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        if (userDao == null) {
            throw new IllegalArgumentException("UserDao cannot be null");
        }
        this.userDao = userDao;
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    public Optional<User> getUserById(int id) throws SQLException {
        return userDao.findById(id);
    }

    public Optional<User> getUserByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userDao.findByUsername(username);
    }

    public User addUser(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userDao.insert(new User(username));
    }
}
