package com.ssafy.pjt1.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.subscription.SubscriptionDto;
import com.ssafy.pjt1.model.dto.user.UserDto;

public interface UserService {
    public UserDto login(UserDto userDto);

    public boolean join(UserDto userDto);

    public int emailCheck(String user_email);

    public String getId();

    public void updateAuthKey(Map<String, String> map);

    public void updateAuthStatus(Map<String, String> map);

    public UserDto userInfo(String user_id);

    public void updatePw(Map<String, String> map);

    public int userModify(UserDto userDto);

    public int userDelete(String user_id);

    public List<SubscriptionDto> getSubBoards(String user_id);

    public List<Map<String, String>> getComments(String user_id);

    public List<Map<String, String>> getPosts(String user_id);

    public List<Map<String, String>> getScraps(String user_id);

    public int favorite(Map<String, String> map);

    public int deleteSubscribe(Map<String, String> map);

    public boolean quizCheck(String answer);

    public void joinCuration(Map<String, Object> cMap);

    public UserDto userDtoById(String user_id);
}
