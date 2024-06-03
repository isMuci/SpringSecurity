package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Permission;
import com.example.entity.User;
import com.example.mapper.PermissionMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user=userMapper.selectOne(queryWrapper);

        if(user==null){
            throw new UsernameNotFoundException("用户名未找到");
        }
        System.out.println("找到用户");
        QueryWrapper<Permission> permissionQueryWrapper=new QueryWrapper<>();
        permissionQueryWrapper.eq("user_id",user.getId());

        List<Permission> permissions = permissionMapper.selectList(permissionQueryWrapper);

        List<String> permissionTags = permissions.stream().map(Permission::getTag).collect(Collectors.toList());
        user.setAuthorities(AuthorityUtils.createAuthorityList(permissionTags));
        System.out.println(user.isEnabled());
        return user;
    }
}
