package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.TokenRepo;
import com.example.entity.User;

public interface UserService extends IService<User> {
    TokenRepo login(User user);

    TokenRepo register(User user);

    String refresh(String refresh);
}
