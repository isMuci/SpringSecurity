package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    Set<Menu> selectMenuByIds(Set<Long> roleIds);
}
