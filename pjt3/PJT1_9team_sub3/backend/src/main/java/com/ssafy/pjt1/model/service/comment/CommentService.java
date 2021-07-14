package com.ssafy.pjt1.model.service.comment;

import java.util.Map;

import com.ssafy.pjt1.model.dto.comment.CommentDto;

public interface CommentService {

	public void createComment(CommentDto commentDto);

	public int commentModify(CommentDto commentDto);

	public int commentDelete(int comment_id);

	public int isCommentWriter(Map<String, Object> map);

	public String getPostIdByCommentId(int comment_id);

}
