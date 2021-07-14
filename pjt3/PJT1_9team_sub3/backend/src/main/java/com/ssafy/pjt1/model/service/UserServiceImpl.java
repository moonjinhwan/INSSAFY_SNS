package com.ssafy.pjt1.model.service;

import com.ssafy.pjt1.model.dto.subscription.SubscriptionDto;
import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.mapper.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SqlSession sqlSession;

	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDto login(UserDto userDto) {
		return sqlSession.getMapper(UserMapper.class).login(userDto);
	}

	@Override
	public boolean join(UserDto userDto) {
		return sqlSession.getMapper(UserMapper.class).join(userDto) == 1;
	}

	@Override
	public int emailCheck(String user_email) {
		return sqlSession.getMapper(UserMapper.class).emailCheck(user_email);
	}

	@Override
	public String getId() {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		int num = 0;

		while (buffer.length() < 13) {
			num = random.nextInt(10);
			buffer.append(num);
		}

		return buffer.toString();
	}

	@Override
	public void updateAuthKey(Map<String, String> map) {
		sqlSession.getMapper(UserMapper.class).updateAuthKey(map);

	}

	@Override
	public void updateAuthStatus(Map<String, String> map) {
		sqlSession.getMapper(UserMapper.class).updateAuthStatus(map);
	}

	@Override
	public UserDto userInfo(String user_email) {
		return sqlSession.getMapper(UserMapper.class).userInfo(user_email);
	}

	@Override
	public void updatePw(Map<String, String> map) {
		sqlSession.getMapper(UserMapper.class).updatePw(map);
	}

	@Override
	public int userModify(UserDto userDto) {
		return sqlSession.getMapper(UserMapper.class).userModify(userDto);
	}

	@Override
	public int userDelete(String user_id) {
		return sqlSession.getMapper(UserMapper.class).userDelete(user_id);
	}

	@Override
	public List<SubscriptionDto> getSubBoards(String user_id) {
		return sqlSession.getMapper(UserMapper.class).getSubBoards(user_id);
	}

	@Override
	public List<Map<String, String>> getComments(String user_id) {
		return sqlSession.getMapper(UserMapper.class).getComments(user_id);
	}

	@Override
	public List<Map<String, String>> getPosts(String user_id) {
		return sqlSession.getMapper(UserMapper.class).getPosts(user_id);
	}

	@Override
	public List<Map<String, String>> getScraps(String user_id) {
		return sqlSession.getMapper(UserMapper.class).getScraps(user_id);
	}

	@Override
	public int favorite(Map<String, String> map) {
		return sqlSession.getMapper(UserMapper.class).favorite(map);
	}

	@Override
	public int deleteSubscribe(Map<String, String> map) {
		return sqlSession.getMapper(UserMapper.class).deleteSubscribe(map);
	}

	@Override
	public boolean quizCheck(String answer) {
		return sqlSession.getMapper(UserMapper.class).quizCheck(answer) == 1;
	}

	@Override
	public void joinCuration(Map<String, Object> cMap) {
		sqlSession.getMapper(UserMapper.class).joinCuration(cMap);
	}

	public UserDto userDtoById(String user_id) {
		return sqlSession.getMapper(UserMapper.class).userDtoById(user_id);
	}
}