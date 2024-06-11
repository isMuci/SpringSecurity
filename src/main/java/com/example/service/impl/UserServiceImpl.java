package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private TokenRepoMapper tokenRepoMapper;

    @Autowired
    private UserMapper userMapper;


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
        tokenMap.put("refresh", new BCryptPasswordEncoder().encode(((User) authenticate.getPrincipal()).getPassword()));
        //目前来看refreshtoken没有存储的必要，解析过后解密密码和数据库密码比对即可
        String refreshToken = JWTUtil.refreshToken(tokenMap, 30L);
        Date exp = DateUtil.offsetDay(new Date(), 7);
        log.info("loginUser : {}", tokenMap);

        TokenRepo token = new TokenRepo(user.getUsername(), accessToken, refreshToken, exp);
        tokenRepoMapper.insert(token);
        return token;
    }

    @Override
    public TokenRepo register(User user) {
        // 获取返回的用户
        User loginUser = userMapper.selectUserByUsernameNoRole(user.getUsername());

        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id", loginUser.getId());
        tokenMap.put("username", loginUser.getUsername());

        String accessToken = JWTUtil.accessToken(tokenMap, 7L);
        tokenMap.put("refresh", new BCryptPasswordEncoder().encode(user.getPassword()));
        //目前来看refreshtoken没有存储的必要，解析过后解密密码和数据库密码比对即可
        String refreshToken = JWTUtil.refreshToken(tokenMap, 30L);
        Date exp = DateUtil.offsetDay(new Date(), 7);
        log.info("loginUser : {}", tokenMap);

        TokenRepo token = new TokenRepo(user.getUsername(), accessToken, refreshToken, exp);
        tokenRepoMapper.insert(token);
        return token;
    }

    @Override
    public String refresh(String refreshToken) {
        Map<String, Claim> claims = JWTUtil.refreshTokenVerify(refreshToken);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, claims.get("id").asInt()));
        String refresh = claims.get("refresh").asString();
        System.out.println(user.getPassword());
        System.out.println(refresh);
        Boolean verify = new BCryptPasswordEncoder().matches(user.getPassword(), refresh);
        if (!verify) return null;
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", user.getId());
        tokenMap.put("username", user.getUsername());

        String accessToken = JWTUtil.accessToken(tokenMap, 7L);
        Date exp = DateUtil.offsetDay(new Date(), 7);
        log.info("loginUser : {}", tokenMap);

        TokenRepo token = new TokenRepo(user.getUsername(), accessToken, refreshToken, exp);
        tokenRepoMapper.insert(token);

        return accessToken;
    }
}
