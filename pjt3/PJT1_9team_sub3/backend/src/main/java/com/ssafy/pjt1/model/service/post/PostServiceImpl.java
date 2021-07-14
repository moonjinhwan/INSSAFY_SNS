package com.ssafy.pjt1.model.service.post;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.comment.CommentDto;
import com.ssafy.pjt1.model.dto.post.PostDto;
import com.ssafy.pjt1.model.mapper.PostMapper;
import com.ssafy.pjt1.model.service.redis.RedisService;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private RedisService redisService;

	@Autowired
	private SqlSession sqlSession;

	public static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Override
	public void createPost(PostDto postDto) {
		sqlSession.getMapper(PostMapper.class).createPost(postDto);
		///////////////////////////////////////////////// post등록시 redisDto에 저장
		redisService.boardPostSortSet(String.valueOf(postDto.getBoard_id()));
		int board_state = sqlSession.getMapper(PostMapper.class).getBoardState(postDto.getBoard_id());
		if(board_state==5){
			logger.info("redis에 기술스택 추가");
			// post_header 기술 스택 redis 1씩 증가
			redisService.postTechStackPlus(String.valueOf(postDto.getPost_header()));
		}
	}

	@Override
	public PostDto getPostById(int post_id) {
		return sqlSession.getMapper(PostMapper.class).getPostById(post_id);
	}

	@Override
	public int postModify(PostDto postDto) {
		int board_state = sqlSession.getMapper(PostMapper.class).getBoardState(postDto.getBoard_id());
		if(board_state==5){
			logger.info("redis에 기술스택 수정");
			// 기존 기술 스택 redis 1씩 감소
			String post_header = sqlSession.getMapper(PostMapper.class).getHeaderByID(postDto.getPost_id());
			redisService.postTechStackMinus(post_header);
			// 수정한 기술 스택 redis 1씩 증가
			redisService.postTechStackPlus(String.valueOf(postDto.getPost_header()));
		}
		return sqlSession.getMapper(PostMapper.class).postModify(postDto);
	}

	@Override
	public int stateModify(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).stateModify(map);
	}

	@Override
	public int postDelete(int post_id) {
		// boardPostDto의 redis 안에 value값 1감소
		redisService.boardPostSortSetDecrease(post_id);
		int board_state = sqlSession.getMapper(PostMapper.class).getBoardStateById(post_id);
		if(board_state==5){
			logger.info("redis에 기술스택 감소");
			// post_header 기술 스택 redis 1씩 감소
			String post_header = sqlSession.getMapper(PostMapper.class).getHeaderByID(post_id);
			redisService.postTechStackMinus(post_header);
		}
		return sqlSession.getMapper(PostMapper.class).postDelete(post_id);
	}

	@Override
	public int isScrapped(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).isScrapped(map);
	}

	@Override
	public void scrap(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).scrap(map);
	}

	@Override
	public void deleteScrap(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).deleteScrap(map);
	}

	@Override
	public int isUnScrapped(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).isUnScrapped(map);
	}

	@Override
	public void updateScrap(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).updateScrap(map);
	}

	@Override
	public int isLiked(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).isLiked(map);
	}

	@Override
	public void like(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).like(map);
	}

	@Override
	public int isUnLiked(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).isUnLiked(map);
	}

	@Override
	public void updateLike(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).updateLike(map);

	}

	@Override
	public void plusCount(int post_id, int board_id) {
		sqlSession.getMapper(PostMapper.class).plusCount(post_id);
		// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>redis chache서버에 저장
		redisService.PostLikeSortSet(post_id);
		// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 추가기능 유저랭킹
		redisService.increaseUserRank(post_id, board_id);
	}

	@Override
	public void unlike(Map<String, Object> map) {
		sqlSession.getMapper(PostMapper.class).unlike(map);
	}

	@Override
	public void minusCount(int post_id, int board_id) {
		sqlSession.getMapper(PostMapper.class).minusCount(post_id);
		//// >>>>>>>>>>>>>>>>>>>>>>>>>>>redis chache서버에 싫어요 1 감소
		redisService.postLikeDecrease(post_id);
		// >>>>>>>>>>>>>>>>>>>>>>>>>>>>> 추가기능 유저랭킹 감소
		redisService.decreaseUserRank(post_id, board_id);
	}

	@Override
	public int getPostLikeCount(int post_id) {
		return sqlSession.getMapper(PostMapper.class).getPostLikeCount(post_id);
	}

	@Override
	public List<Map<String, Object>> getComment(int post_id) {
		return sqlSession.getMapper(PostMapper.class).getComment(post_id);
	}

	@Override
	public List<Map<String, Object>> getPostList(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getPostList(map);
	}

	@Override
	public List<Map<String, Object>> searchPostNew(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostNew(map);
	}

	@Override
	public List<Map<String, Object>> searchPostPopular(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostPopular(map);
	}

	@Override
	public List<Map<String, Object>> boardPostNew(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostNew(map);
	}

	@Override
	public List<Map<String, Object>> boardPostPopular(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostPopular(map);
	}

	@Override
	public void deleteScrapAll(int post_id) {
		sqlSession.getMapper(PostMapper.class).deleteScrapAll(post_id);
	}

	@Override
	public void deleteLikeAll(int post_id) {
		sqlSession.getMapper(PostMapper.class).deleteLikeAll(post_id);
	}

	@Override
	public void deleteCommentAll(int post_id) {
		sqlSession.getMapper(PostMapper.class).deleteCommentAll(post_id);
	}

	@Override
	public List<Map<String, Object>> getSalesList(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getSalesList(map);
	}

	@Override
	public List<Map<String, Object>> marketPostNew(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).marketPostNew(map);
	}

	@Override
	public List<Map<String, Object>> marketPostPopular(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).marketPostPopular(map);
	}

	@Override
	public String getWriterName(String user_id) {
		return sqlSession.getMapper(PostMapper.class).getWriterName(user_id);
	}

	
	@Override
	public String getWriterImage(String user_id) {
		return sqlSession.getMapper(PostMapper.class).getWriterImage(user_id);
	}

	@Override
	public int isWriter(Map<String, Object> map) {
		int res = sqlSession.getMapper(PostMapper.class).isWriter(map);

		return res;
	}

	@Override
	public int getSalesCnt() {
		return sqlSession.getMapper(PostMapper.class).getSalesCnt();
	}

	@Override
	public int searchSalesCnt(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchSalesCnt(map);
	}

	@Override
	public int getTotalPostCnt(int board_id) {
		return sqlSession.getMapper(PostMapper.class).getTotalPostCnt(board_id);
	}

	@Override
	public int getSearchPostCnt(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getSearchPostCnt(map);
	}

	@Override
	public int getAllSearchPostCnt(String keyword) {
		return sqlSession.getMapper(PostMapper.class).getAllSearchPostCnt(keyword);
	}

	
	@Override
	public int getAllSearchPostCntH(String keyword) {
		return sqlSession.getMapper(PostMapper.class).getAllSearchPostCntH(keyword);
	}

	@Override
	public int getAllSearchPostCntT(String keyword) {
		return sqlSession.getMapper(PostMapper.class).getAllSearchPostCntT(keyword);
	}

	@Override
	public int getAllSearchPostCntD(String keyword) {
		return sqlSession.getMapper(PostMapper.class).getAllSearchPostCntD(keyword);
	}
	
	@Override
	public List<Map<String, Object>> searchPostNewH(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostNewH(map);
	}

	@Override
	public List<Map<String, Object>> searchPostNewT(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostNewT(map);
	}

	@Override
	public List<Map<String, Object>> searchPostNewD(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostNewD(map);
	}

	@Override
	public List<Map<String, Object>> searchPostPopularH(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostPopularH(map);
	}

	@Override
	public List<Map<String, Object>> searchPostPopularT(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostPopularT(map);
	}

	@Override
	public List<Map<String, Object>> searchPostPopularD(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).searchPostPopularD(map);
	}

	@Override
	public int getSearchPostCntH(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getSearchPostCntH(map);
	}

	@Override
	public int getSearchPostCntT(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getSearchPostCntT(map);
	}

	@Override
	public int getSearchPostCntD(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).getSearchPostCntD(map);
	}

	@Override
	public List<Map<String, Object>> boardPostNewH(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostNewH(map);
	}

	@Override
	public List<Map<String, Object>> boardPostNewT(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostNewT(map);
	}

	@Override
	public List<Map<String, Object>> boardPostNewD(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostNewD(map);
	}

	@Override
	public List<Map<String, Object>> boardPostPopularH(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostPopularH(map);
	}

	@Override
	public List<Map<String, Object>> boardPostPopularT(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostPopularT(map);
	}

	@Override
	public List<Map<String, Object>> boardPostPopularD(Map<String, Object> map) {
		return sqlSession.getMapper(PostMapper.class).boardPostPopularD(map);
	}

}
