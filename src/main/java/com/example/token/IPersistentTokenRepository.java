package com.example.token;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.entity.SpringSecurityToken;
import com.example.mapper.SpringSecurityTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IPersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    SpringSecurityTokenMapper tokenMapper;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        SpringSecurityToken springSecurityToken = new SpringSecurityToken();
        springSecurityToken.setUsername(token.getUsername());
        springSecurityToken.setSeries(token.getSeries());
        springSecurityToken.setTokenValue(token.getTokenValue());
        springSecurityToken.setDate(token.getDate());

        tokenMapper.insert(springSecurityToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        UpdateWrapper<SpringSecurityToken> updateWrapper=new UpdateWrapper<>();
        updateWrapper.set("token",tokenValue);
        updateWrapper.set("date",lastUsed);

        updateWrapper.eq("series",series);

        tokenMapper.update(updateWrapper);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        SpringSecurityToken springSecurityToken = tokenMapper.selectOne(new QueryWrapper<SpringSecurityToken>().eq("series", seriesId));

        if(springSecurityToken==null){
            return null;
        }

        return new PersistentRememberMeToken(springSecurityToken.getUsername(),springSecurityToken.getSeries(),springSecurityToken.getTokenValue(),springSecurityToken.getDate());
    }

    @Override
    public void removeUserTokens(String username) {
        UpdateWrapper<SpringSecurityToken> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("username",username);
        tokenMapper.delete(updateWrapper);
    }
}
