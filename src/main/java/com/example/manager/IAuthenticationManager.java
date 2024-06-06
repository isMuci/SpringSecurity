package com.example.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.Menu;
import com.example.mapper.MenuMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
public class IAuthenticationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        String requestURI = request.getRequestURI();
        log.info("check.requestURI : {}",requestURI);
        if("/login".equals(requestURI)||"/register".equals(requestURI)){
            return new AuthorizationDecision(true);
        }
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>().eq(Menu::getPath, requestURI));
        log.info("check.menu : {}",menu);
        if(menu==null){
            return new AuthorizationDecision(false);
        }
        String menuPerm = menu.getPerms();
        log.info("check.menuPerm : {}",menuPerm);
        if(menuPerm.equals("*")){
            return new AuthorizationDecision(true);
        }
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
        List<String> userPerms = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        log.info("check.userPerms : {}",userPerms);
        if(userPerms.contains(menuPerm)){
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }
}
