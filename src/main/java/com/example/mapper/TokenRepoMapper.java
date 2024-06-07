package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.TokenRepo;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenRepoMapper extends BaseMapper<TokenRepo> {
}
