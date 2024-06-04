package com.example.controller;

import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    SessionRegistry sessionRegistry;

    @ResponseBody
    @GetMapping("/index")
    public String index(){
        System.out.println("index");
        return "index";
    }

    @GetMapping("/kickOut")
    public String kickOut(String username){
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object allPrincipal : allPrincipals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(allPrincipal, false);

            User user=(User)allPrincipal;
            if(user.getUsername().equals(username)){
                sessions.forEach(SessionInformation::expireNow);
            }
        }
        return "index";
    }

    @GetMapping("/admin/api")
    public String admin(){
        return "admin";
    }

    @GetMapping("/user/api")
    public String user(){
        return "user";
    }

    @GetMapping("/app/api")
    public String app(){
        return "app";
    }

    @GetMapping("/noAuth")
    public String noAuth(){
        return "noAuth";
    }


}
