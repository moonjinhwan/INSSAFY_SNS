package com.ssafy.pjt1.model.service;

import com.ssafy.pjt1.model.dao.UserDao;
import com.ssafy.pjt1.model.dto.UserDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public boolean loginDao(UserDto userDto) {
        String email = sqlSession.getMapper(UserDao.class).loginDao(userDto);
        return email.equals(userDto.getEmail());
    }

    @Override
    public boolean joinService(UserDto userDto) {
        return sqlSession.getMapper(UserDao.class).joinDao(userDto) == 1;
    }

}