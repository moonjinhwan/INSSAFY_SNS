package com.ssafy.pjt1.model.service;

import com.ssafy.pjt1.model.dto.UserDto;

public interface UserService {
    public boolean loginDao(UserDto userDto);

    public boolean joinService(UserDto userDto);
}
