package com.example.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Slf4j
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {
    @Autowired
    private Producer producer;

    @SneakyThrows
    @GetMapping("/kaptcha.jpg")
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("image/jpeg");
        String kaptchaText=producer.createText();
        log.info("验证码：{}",kaptchaText);
        request.getSession().setAttribute("kaptcha",kaptchaText);
        BufferedImage image=producer.createImage(kaptchaText);
        ServletOutputStream out=response.getOutputStream();

        ImageIO.write(image,"jpg",out);
        out.flush();
    }
}
