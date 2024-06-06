package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("menu")
public class Menu implements Serializable {

    @TableId
    private Long id;
    private Long parentId;
    private String menuName;
    private Integer sort;
    private String path;
    private String perms;
    private Integer menuType;
    private String icon;
    @TableLogic
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
