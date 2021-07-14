package com.ssafy.pjt1.model.mapper;

import java.util.List;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.post.PostDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

    public List<BoardDto> selectFavorite(String user_id);

    public int getSubsriptionNumber(String board_id);

    public List<PostDto> getRecentFive(String board_id);
}
