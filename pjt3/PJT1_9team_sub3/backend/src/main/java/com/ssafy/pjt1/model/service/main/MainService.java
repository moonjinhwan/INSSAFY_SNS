package com.ssafy.pjt1.model.service.main;

import java.util.List;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.subscription.SubscriptionDto;

public interface MainService {

    public List<BoardDto> selectFavorite(String user_id);

    public void updateSubscriptionCache();

    public int getSubsriptionNumber(String board_id);

    public void updatePostSort();

    public void updatePostLikeSort();
}
