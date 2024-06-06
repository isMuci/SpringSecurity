package com.example.token;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.entity.SpringSecurityToken;
import com.example.mapper.SpringSecurityTokenMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
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
        log.info("createNewToken.springSecurityToken : {}",springSecurityToken);
        tokenMapper.insert(springSecurityToken);
        log.info("createNewToken.tokenMapper : {}",tokenMapper);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        tokenMapper.update(new LambdaUpdateWrapper<SpringSecurityToken>()
                .set(SpringSecurityToken::getTokenValue,tokenValue)
                .set(SpringSecurityToken::getDate,lastUsed)
                .eq(SpringSecurityToken::getSeries,series));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        SpringSecurityToken springSecurityToken = tokenMapper.selectOne(new LambdaQueryWrapper<SpringSecurityToken>().eq(SpringSecurityToken::getSeries, seriesId));
        return new PersistentRememberMeToken(springSecurityToken.getUsername(),springSecurityToken.getSeries(),springSecurityToken.getTokenValue(),springSecurityToken.getDate());
    }

    @Override
    public void removeUserTokens(String username) {
        tokenMapper.delete(new LambdaUpdateWrapper<SpringSecurityToken>().eq(SpringSecurityToken::getUsername,username));
    }
}
