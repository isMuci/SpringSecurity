package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("spring_security_tokens")
@Data
public class SpringSecurityToken {

    @TableField(value = "username")
    private String username;

    @TableField(value = "series")
    private String series;

    @TableField(value = "accessToken")
    private String tokenValue;

    @TableField(value = "last_used")
    private Date date;
}
