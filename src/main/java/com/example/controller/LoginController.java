package com.example.controller;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserDetailService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/register")
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody User user){
        System.out.println("登录中");
        String token = userService.login(user);

        return token;
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user){
        System.out.println("注册中");
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userMapper.insert(user);

        return "注册成功";
    }
}
