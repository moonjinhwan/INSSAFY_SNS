package com.ssafy.pjt1.model.service.comment;

import java.util.Map;

import com.ssafy.pjt1.model.dto.comment.CommentDto;
import com.ssafy.pjt1.model.mapper.CommentMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private CommentMapper commentMapper;

	public static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Override
	public void createComment(CommentDto commentDto) {
		sqlSession.getMapper(CommentMapper.class).createComment(commentDto);

	}

	@Override
	public int commentModify(CommentDto commentDto) {
		return sqlSession.getMapper(CommentMapper.class).commentModify(commentDto);
	}

	@Override
	public int commentDelete(int comment_id) {
		return sqlSession.getMapper(CommentMapper.class).commentDelete(comment_id);
	}

	@Override
	public int isCommentWriter(Map<String, Object> map) {
		return sqlSession.getMapper(CommentMapper.class).isCommentWriter(map);
	}

	@Override
	public String getPostIdByCommentId(int comment_id) {
		return commentMapper.getPostIdByCommentId(comment_id);
	}

}
