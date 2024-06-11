package com.example.controller;

import com.example.entity.TokenRepo;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserDetailService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Controller
//@RequestMapping("/register")
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public TokenRepo login(@RequestParam("username")String username,@RequestParam("password")String password){
        System.out.println("登录中");
        TokenRepo token = userService.login(new User(username,password));

        return token;
    }

    @PostMapping("/register")
    @ResponseBody
    public TokenRepo register(@RequestBody User user){
        System.out.println("注册中");
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userMapper.insert(user);
        log.info("{}",user);
        TokenRepo token = userService.register(user);
        return token;
    }

    @PostMapping("/refresh")
    @ResponseBody
    public String refresh(@RequestBody Map refresh) {
        System.out.println(refresh);
        String token = userService.refresh(refresh.get("refresh").toString());
        return token;
    }
}
