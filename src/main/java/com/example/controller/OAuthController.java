package com.example.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.example.entity.OAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("oauth")
public class OAuthController {

    @GetMapping("notify")
    @ResponseBody
    public String gitee(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        map.put("client_id", "9cecc02909cd71cc217c41fe5635755720544c385e352e698a38ff145318c169");
        map.put("client_secret", "d57adb6d5ccd851134b7a490cb9c39313031b23111ed8e0a6ac3aaf50dbc4532");
        map.put("redirect_uri", "http://localhost:8080/oauth/notify");
        // 通过code获取access_token
        String post = HttpUtil.post("https://gitee.com/oauth/token", map);
        OAuth oAuth = JSONUtil.toBean(post, OAuth.class);
        // 获取用户信息
        String userInfoStr = HttpUtil.get("https://gitee.com/api/v5/user?access_token=" + oAuth.getAccessToken());
        // 将用户信息转换为实体
        String name = (String) JSONUtil.parseObj(userInfoStr).get("name");
        return name;
    }
}
