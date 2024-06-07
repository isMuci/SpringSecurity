package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.TokenRepo;
import com.example.entity.User;
import com.example.mapper.TokenRepoMapper;
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

    @Autowired
    private TokenRepoMapper tokenRepoMapper;


    //TODO MUCI 2024/6/7: 将token持久化存储
    @Override
    public TokenRepo login(User user) {
        UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate  = null;
        try{
            authenticate = authenticationManager.authenticate(authentication);
        }
        catch (AuthenticationException e) {
            log.info("login.AuthenticationException : {}",e.getMessage());
            // TODO 抛出一个业务异常
            return null;
        }
        // 获取返回的用户
        User loginUser = (User) authenticate.getPrincipal();
        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id",((User) authenticate.getPrincipal()).getId());
        tokenMap.put("username",((User) authenticate.getPrincipal()).getUsername());

        String accessToken = JWTUtil.accessToken(tokenMap, 7L);
        String refreshToken = JWTUtil.refreshToken(tokenMap, 30L);

        log.info("loginUser : {}", tokenMap);

        TokenRepo token=new TokenRepo(user.getUsername(),accessToken,refreshToken);
        tokenRepoMapper.update(new LambdaUpdateWrapper<TokenRepo>().set(TokenRepo::getAccessToken,accessToken).set(TokenRepo::getRefreshToken,refreshToken).eq(TokenRepo::getUsername,user.getUsername()));
        return token;
    }

    @Override
    public TokenRepo register(User user) {
        UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate  = null;
        try{
            authenticate = authenticationManager.authenticate(authentication);
        }
        catch (AuthenticationException e) {
            log.info("login.AuthenticationException : {}",e.getMessage());
            // TODO 抛出一个业务异常
            return null;
        }
        // 获取返回的用户
        User loginUser = (User) authenticate.getPrincipal();
        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id",((User) authenticate.getPrincipal()).getId());
        tokenMap.put("username",((User) authenticate.getPrincipal()).getUsername());

        String accessToken = JWTUtil.accessToken(tokenMap, 7L);
        String refreshToken = JWTUtil.refreshToken(tokenMap, 30L);

        log.info("loginUser : {}", tokenMap);

        TokenRepo token=new TokenRepo(user.getUsername(),accessToken,refreshToken);
        tokenRepoMapper.insert(token);
        return token;
    }
}
