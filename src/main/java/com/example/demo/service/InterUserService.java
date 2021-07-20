package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.*;

public interface InterUserService {
    List<User> findAll();
    Optional<User> finById(int id);
    void deleteById(int id);
    void saveUser(User user);
    User findUserByIp(String ip);
}
