package com.example.test2.service;

import com.example.test2.model.User;
import java.util.List;

public interface UserService {
    List<User> findAllUser();

    List<User> getByUserName(String username);
}
