package com.ssafy.pjt1.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private SqlSession sqlSession;

	public static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	@Override
	public void createBoard(BoardDto boardDto) {
		sqlSession.getMapper(BoardMapper.class).createBoard(boardDto);
	}

	@Override
	public void subscribe(Map<String, Object> map) {
		sqlSession.getMapper(BoardMapper.class).subscribe(map);
	}

	@Override
	public void addFunction(Map<String, Object> map) {
		sqlSession.getMapper(BoardMapper.class).addFunction(map);

	}

	@Override
	public int isSubscribed(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).isSubscribed(map);
	}

	@Override
	public void unsubscribe(Map<String, Object> map) {
		sqlSession.getMapper(BoardMapper.class).unsubscribe(map);

	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).searchUser(keyword);
	}

	@Override
	public void updateManager(Map<String, Object> map) {
		sqlSession.getMapper(BoardMapper.class).updateManager(map);
	}

	@Override
	public int modifyBoard(BoardDto boardDto) {
		return sqlSession.getMapper(BoardMapper.class).modifyBoard(boardDto);
	}

	@Override
	public List<Map<String, Object>> getBoardsNew(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).getBoardsNew(map);
	}

	@Override
	public List<Map<String, Object>> getBoardsPopular(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).getBoardsPopular(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardNew(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardNew(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardPopular(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardPopular(map);
	}

	@Override
	public int deleteBoard(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).deleteBoard(board_id);
	}

	@Override
	public BoardDto detailBoard(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).detailBoard(board_id);
	}

	@Override
	public int isUnSubscribed(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).isUnSubscribed(map);
	}

	@Override
	public void updateSubscribe(Map<String, Object> map) {
		sqlSession.getMapper(BoardMapper.class).updateSubscribe(map);
	}

	@Override
	public void deleteBoardAll(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deleteBoardAll(board_id);
	}

	@Override
	public void deleteSubscription(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deleteSubscription(board_id);
	}

	@Override
	public void deletePostAll(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deletePostAll(board_id);
	}

	@Override
	public void deleteCalendar(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deleteCalendar(board_id);
	}

	@Override
	public void deleteCheckList(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deleteCheckList(board_id);
	}

	@Override
	public void deleteVote(int board_id) {
		sqlSession.getMapper(BoardMapper.class).deleteVote(board_id);
	}

	@Override
	public List<Integer> getVoteList(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).getVoteList(board_id);
	}

	@Override
	public List<Integer> getPostList(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).getPostList(board_id);
	}

	@Override
	public int getBoardCount(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).getBoardCount(board_id);
	}

	@Override
	public int getIdbyPostId(int post_id) {
		return sqlSession.getMapper(BoardMapper.class).getIdbyPostId(post_id);
	}

	@Override
	public int isManager(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).isManager(map);
	}

	@Override
	public Map<String, String> getBoardInfo(String board_id) {
		return sqlSession.getMapper(BoardMapper.class).getBoardInfo(board_id);
	}

	@Override
	public int getTotalCnt() {
		return sqlSession.getMapper(BoardMapper.class).getTotalCnt();
	}

	@Override
	public int getSearchCnt(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).getSearchCnt(keyword);
	}

	@Override
	public Map<String, Object> boardFunc(int board_id) {
		return sqlSession.getMapper(BoardMapper.class).boardFunc(board_id);
	}

	@Override
	public void addChecklist(Map<String, Object> map2) {
		sqlSession.getMapper(BoardMapper.class).addChecklist(map2);
	}

	@Override
	public void addCalendar(Map<String, Object> map2) {
		sqlSession.getMapper(BoardMapper.class).addCalendar(map2);
	}

	@Override
	public void addVote(Map<String, Object> map2) {
		sqlSession.getMapper(BoardMapper.class).addVote(map2);
	}

	@Override
	public void addUserRank(Map<String, Object> map2) {
		sqlSession.getMapper(BoardMapper.class).addUserRank(map2);
	}

	@Override
	public int getSearchCntN(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).getSearchCntN(keyword);
	}

	@Override
	public int getSearchCntD(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).getSearchCntD(keyword);
	}

	@Override
	public int getSearchCntL(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).getSearchCntL(keyword);
	}

	@Override
	public int getSearchCntH(String keyword) {
		return sqlSession.getMapper(BoardMapper.class).getSearchCntH(keyword);
	}

	@Override
	public List<Map<String, Object>> searchBoardNewN(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardNewN(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardNewD(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardNewD(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardNewL(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardNewL(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardNewH(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardNewH(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardPopularN(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardPopularN(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardPopularD(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardPopularD(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardPopularL(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardPopularL(map);
	}

	@Override
	public List<Map<String, Object>> searchBoardPopularH(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).searchBoardPopularH(map);
	}

}
