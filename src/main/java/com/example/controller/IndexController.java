package com.example.controller;

import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ResponseBody
    @GetMapping("/kickOut")
    public String kickOut(@RequestParam(name = "username") String username){
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object allPrincipal : allPrincipals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(allPrincipal, false);

            User user=(User)allPrincipal;
            if(user.getUsername().equals(username)){
                sessions.forEach(SessionInformation::expireNow);
            }
        }
        return "kickOut";
    }

    @ResponseBody
    @GetMapping("/admin/api")
    public String admin(){
        return "admin";
    }

    @ResponseBody
    @GetMapping("/user/api")
    public String user(){
        return "user";
    }

    @ResponseBody
    @GetMapping("/app/api")
    public String app(){
        return "app";
    }

    @ResponseBody
    @GetMapping("/noAuth")
    public String noAuth(){
        return "noAuth";
    }


}
