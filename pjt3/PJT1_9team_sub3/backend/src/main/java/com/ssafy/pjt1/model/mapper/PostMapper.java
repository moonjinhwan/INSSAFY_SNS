package com.ssafy.pjt1.model.mapper;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.comment.CommentDto;
import com.ssafy.pjt1.model.dto.post.PostDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

	public void createPost(PostDto postDto);

	public PostDto getPostById(int post_id);

	public int postModify(PostDto postDto);

	public int stateModify(Map<String, Object> map);

	public int postDelete(int post_id);

	public int isScrapped(Map<String, Object> map);

	public void scrap(Map<String, Object> map);

	public void deleteScrap(Map<String, Object> map);

	public int isUnScrapped(Map<String, Object> map);

	public void updateScrap(Map<String, Object> map);

	public int isLiked(Map<String, Object> map);

	public void like(Map<String, Object> map);

	public int isUnLiked(Map<String, Object> map);

	public void updateLike(Map<String, Object> map);

	public void plusCount(int post_id);

	public void unlike(Map<String, Object> map);

	public void minusCount(int post_id);

	public int getPostLikeCount(int post_id);

	public List<Map<String, Object>> getComment(int post_id);

	public List<Map<String, Object>> getPostList(Map<String, Object> map);

	public List<Map<String, Object>> searchPostNew(Map<String, Object> map);

	public List<Map<String, Object>> searchPostPopular(Map<String, Object> map);

	public List<Map<String, Object>> boardPostNew(Map<String, Object> map);

	public List<Map<String, Object>> boardPostPopular(Map<String, Object> map);

	public void deleteScrapAll(int post_id);

	public void deleteLikeAll(int post_id);

	public void deleteCommentAll(int post_id);

	public List<Map<String, Object>> getSalesList(Map<String, Object> map);

	public List<Map<String, Object>> marketPostNew(Map<String, Object> map);

	public List<Map<String, Object>> marketPostPopular(Map<String, Object> map);

	public String getWriterName(String user_id);

	public int isWriter(Map<String, Object> map);

	public int getSalesCnt();

	public int searchSalesCnt(Map<String, Object> map);

	public int getTotalPostCnt(int board_id);

	public int getSearchPostCnt(Map<String, Object> map);

	public int getAllSearchPostCnt(String keyword);

	public String getWriterImage(String user_id);

	public String getHeaderByID(int post_id);

	public int getBoardState(int board_id);

	public int getBoardStateById(int post_id);

	public int getAllSearchPostCntH(String keyword);

	public int getAllSearchPostCntT(String keyword);

	public int getAllSearchPostCntD(String keyword);

	public List<Map<String, Object>> searchPostNewH(Map<String, Object> map);

	public List<Map<String, Object>> searchPostNewT(Map<String, Object> map);

	public List<Map<String, Object>> searchPostNewD(Map<String, Object> map);

	public List<Map<String, Object>> searchPostPopularH(Map<String, Object> map);

	public List<Map<String, Object>> searchPostPopularT(Map<String, Object> map);

	public List<Map<String, Object>> searchPostPopularD(Map<String, Object> map);

	public int getSearchPostCntH(Map<String, Object> map);

	public int getSearchPostCntT(Map<String, Object> map);

	public int getSearchPostCntD(Map<String, Object> map);

	public List<Map<String, Object>> boardPostNewH(Map<String, Object> map);

	public List<Map<String, Object>> boardPostNewT(Map<String, Object> map);

	public List<Map<String, Object>> boardPostNewD(Map<String, Object> map);

	public List<Map<String, Object>> boardPostPopularH(Map<String, Object> map);

	public List<Map<String, Object>> boardPostPopularT(Map<String, Object> map);

	public List<Map<String, Object>> boardPostPopularD(Map<String, Object> map);

}
