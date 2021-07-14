package com.ssafy.pjt1.model.mapper;

import java.util.Map;

import com.ssafy.pjt1.model.dto.comment.CommentDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

	public void createComment(CommentDto commentDto);

	public int commentModify(CommentDto commentDto);

	public int commentDelete(int comment_id);

	public int isCommentWriter(Map<String, Object> map);

	public String getPostIdByCommentId(int comment_id);
}
