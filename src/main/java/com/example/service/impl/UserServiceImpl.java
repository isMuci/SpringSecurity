package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Permission;
import com.example.entity.User;
import com.example.mapper.PermissionMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserDetailService;
import com.example.service.UserService;
import com.example.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            log.error("用户名或密码错误！");
            log.info("{}",e.getMessage());
            System.out.println(e.getMessage());
            // TODO 抛出一个业务异常
            return "用户名或密码错误！";
        }
        // 获取返回的用户
        User umsSysUser = (User) authenticate.getPrincipal();
        // 生成一个token，返回给前端
        String token = JWTUtil.token(authenticate, 7L);
        log.info("登陆后的用户==========》{}",umsSysUser);
        return token;
    }
}
