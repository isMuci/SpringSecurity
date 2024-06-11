package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@TableName("tokenrepo")
public class TokenRepo {
    private String username;
    private String accessToken;
    private String refreshToken;
    private Date exp;
}
