package com.example.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.Menu;
import com.example.entity.Role;
import com.example.entity.TokenRepo;
import com.example.entity.User;
import com.example.mapper.MenuMapper;
import com.example.mapper.TokenRepoMapper;
import com.example.mapper.UserMapper;
import com.example.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    TokenRepoMapper tokenRepoMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("doFilterInternal.authorization : {}", authorization);

        if(authorization!=null){
            String[] authorizationSplit = authorization.split(" ");
            if (authorizationSplit.length<2){
                System.out.println("缺少token");
            }else {
                String bearer=authorizationSplit[0];
                String token=authorizationSplit[1];
                if (!bearer.equals("Bearer") || token == null) {
                    System.out.println("缺少bearer或token");
                }else{

                    try{
                        Map<String, Claim> claims;
                        claims = JWTUtil.accessTokenVerify(token);
                        String username=claims.get("username").asString();
                        User user = userMapper.selectUserByUsername(username);
                        log.info("loadUserByUsername.user : {}",user);
                        if(user!=null){
                            TokenRepo tokenRepo = tokenRepoMapper.selectOne(new LambdaQueryWrapper<TokenRepo>().eq(TokenRepo::getUsername, username));
                            if(tokenRepo.getAccessToken().equals(token)){
                                Set<Role> roles = user.getRoles();
                                log.info("loadUserByUsername.role : {}",roles);
                                Set<Long> roleIds = roles.stream().map(Role::getRoleId).collect(Collectors.toSet());
                                Set<Menu> menus = menuMapper.selectMenuByIds(roleIds);
                                log.info("loadUserByUsername.menu : {}",menus);
                                user.setPerms(menus.stream().map(Menu::getPerms).collect(Collectors.toSet()));
                                log.info("loadUserByUsername.user : {}",user);

                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    }catch (JWTVerificationException e){
                        System.out.println("token验证失败");
                    }
                }

            }
        }
        filterChain.doFilter(request,response);
    }
}
