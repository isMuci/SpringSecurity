package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private  AuthenticationManager authenticationManager;


    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate  = null;
        try{
            authenticate = authenticationManager.authenticate(authentication);
        }
        catch (AuthenticationException e) {
            log.info("login : {}",e.getMessage());
            // TODO 抛出一个业务异常
            return "用户名或密码错误！";
        }
        // 获取返回的用户
        User loginUser = (User) authenticate.getPrincipal();
        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id",((User) authenticate.getPrincipal()).getId());
        tokenMap.put("username",((User) authenticate.getPrincipal()).getUsername());
        tokenMap.put("perms",((User) authenticate.getPrincipal()).getPerms().stream().toList());

        log.info("loginUser : {}", tokenMap);
        return JWTUtil.token(tokenMap, 7L);
    }
}
