package com.ssafy.pjt1.model.service.study;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.mapper.StudyMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyServiceImpl implements StudyService {

	@Autowired
	private SqlSession sqlSession;

	public static final Logger logger = LoggerFactory.getLogger(StudyServiceImpl.class);

	@Override
	public List<Map<String, Object>> getPromoList(Map<String, Object> map) {
		return sqlSession.getMapper(StudyMapper.class).getPromoList(map);
	}

	@Override
	public List<Integer> getStudyId(String login_id) {
		return sqlSession.getMapper(StudyMapper.class).getStudyId(login_id);
	}

	@Override
	public Map<String, Object> getStudyInfo(Integer board_id) {
		return sqlSession.getMapper(StudyMapper.class).getStudyInfo(board_id);
	}

	@Override
	public List<Map<String, Object>> getAllList(Map<String, Object> map) {
		return sqlSession.getMapper(StudyMapper.class).getAllList(map);
	}

	@Override
	public int getTotalCnt() {
		return sqlSession.getMapper(StudyMapper.class).getTotalCnt();
	}

	@Override
	public void request(Map<String, Object> map) {
		sqlSession.getMapper(StudyMapper.class).request(map);
	}

	@Override
	public List<Map<String, Object>> getRequestList(int board_id) {
		return sqlSession.getMapper(StudyMapper.class).getRequestList(board_id);
	}

	@Override
	public void requestProcess(Map<String, Object> map) {
		sqlSession.getMapper(StudyMapper.class).requestProcess(map);
	}

	@Override
	public void secession(Map<String, Object> map) {
		sqlSession.getMapper(StudyMapper.class).secession(map);
	}

	@Override
	public void reSubscription(Map<String, Object> map) {
		sqlSession.getMapper(StudyMapper.class).reSubscription(map);
	}

	@Override
	public List<Map<String, Object>> myRequestList(String user_id) {
		return sqlSession.getMapper(StudyMapper.class).myRequestList(user_id);
	}

	@Override
	public List<Map<String, Object>> myStudyRequest(Map<String, Object> map) {
		return sqlSession.getMapper(StudyMapper.class).myStudyRequest(map);
	}

}
