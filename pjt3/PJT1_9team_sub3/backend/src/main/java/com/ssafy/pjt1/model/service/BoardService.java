package com.ssafy.pjt1.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.user.UserDto;

public interface BoardService {

	public void createBoard(BoardDto boardDto);

	public void subscribe(Map<String, Object> map);

	public void addFunction(Map<String, Object> map);

	public int isSubscribed(Map<String, Object> map);

	public void unsubscribe(Map<String, Object> map);

	public List<UserDto> searchUser(String keyword);

	public void updateManager(Map<String, Object> map);

	public int modifyBoard(BoardDto boardDto);

	public List<Map<String, Object>> getBoardsNew(Map<String, Object> map);

	public List<Map<String, Object>> getBoardsPopular(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardNew(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardPopular(Map<String, Object> map);

	public int deleteBoard(int board_id);

	public BoardDto detailBoard(int board_id);

	public int isUnSubscribed(Map<String, Object> map);

	public void updateSubscribe(Map<String, Object> map);

	public void deleteBoardAll(int board_id);

	public void deleteSubscription(int board_id);

	public void deletePostAll(int board_id);

	public void deleteCalendar(int board_id);

	public void deleteCheckList(int board_id);

	public void deleteVote(int board_id);

	public List<Integer> getVoteList(int board_id);

	public List<Integer> getPostList(int board_id);

	public int getBoardCount(int board_id);

	public int getIdbyPostId(int post_id);

	public int isManager(Map<String, Object> map);

	public Map<String, String> getBoardInfo(String board_id);

	public int getTotalCnt();

	public int getSearchCnt(String keyword);

	public Map<String, Object> boardFunc(int board_id);

	public void addChecklist(Map<String, Object> map2);

	public void addCalendar(Map<String, Object> map2);

	public void addVote(Map<String, Object> map2);

	public void addUserRank(Map<String, Object> map2);

	public int getSearchCntN(String keyword);

	public int getSearchCntD(String keyword);

	public int getSearchCntL(String keyword);

	public int getSearchCntH(String keyword);

	public List<Map<String, Object>> searchBoardNewN(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardNewD(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardNewL(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardNewH(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardPopularN(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardPopularD(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardPopularL(Map<String, Object> map);

	public List<Map<String, Object>> searchBoardPopularH(Map<String, Object> map);
}
