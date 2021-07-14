package com.ssafy.pjt1.model.dao;

import com.ssafy.pjt1.model.dto.UserDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    public String loginDao(UserDto userDto);

    public int joinDao(UserDto userDto);
}
