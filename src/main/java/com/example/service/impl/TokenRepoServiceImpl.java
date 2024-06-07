package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.TokenRepo;
import com.example.entity.User;
import com.example.mapper.TokenRepoMapper;
import com.example.mapper.UserMapper;
import com.example.service.TokenRepoService;
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
public class TokenRepoServiceImpl extends ServiceImpl<TokenRepoMapper, TokenRepo> implements TokenRepoService {
}
