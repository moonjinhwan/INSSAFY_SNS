package com.ssafy.pjt1.model.service.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.post.PostDto;
import com.ssafy.pjt1.model.mapper.MainMapper;
import com.ssafy.pjt1.model.service.BoardService;
import com.ssafy.pjt1.model.service.post.PostService;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

    @Override
    public List<BoardDto> selectFavorite(String user_id) {
        return sqlSession.getMapper(MainMapper.class).selectFavorite(user_id);
    }

    /*
     * 기능: follow수 업데이트
     * 
     * developer: 문진환
     * 
     */
    @Scheduled(fixedDelay = 60000) // 1분 마다 캐시 갱신
    @Override
    public void updateSubscriptionCache() {
        logger.info("boardFollowRank 캐시 업데이트");
        String key = "boardFollowSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        // follow캐시에서 top3 뽑기
        Set<String> set = zset.reverseRange(key, 0, 2);// board_id 얻어옴
        Map<Map<String, String>, List<PostDto>> resultMap = new HashMap<>();
        for (String board_id : set) {
            // db조회 객체 top3 얻음
            Map<String, String> boardInfo = boardService.getBoardInfo(board_id);
            if (boardInfo != null) {// null이 아닐때만 랭크에 넣어줌
                resultMap.put(boardInfo, sqlSession.getMapper(MainMapper.class).getRecentFive(board_id));
            }
        }
        // boardFollowRank캐시에 넣기
        ObjectMapper mapper = new ObjectMapper();// jackson 라이브러리
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        try {
            String msg = mapper.writeValueAsString(resultMap);
            // logger.info("캐시:{}", msg);
            valueOps.set("boardFollowRank", msg);
            // logger.info(valueOps.get("followRank"));
        } catch (JsonProcessingException e) {
            logger.error("updateSubscriptionCache", e);
        }
    }

    @Override
    public int getSubsriptionNumber(String board_id) {
        return sqlSession.getMapper(MainMapper.class).getSubsriptionNumber(board_id);
    }

    @Scheduled(fixedDelay = 60000) // 1분 마다 캐시 갱신
    @Override
    public void updatePostSort() {
        logger.info("boardPostRank 캐시 업데이트");
        String key = "boardPostSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        // postSort캐시에서 top3 뽑기
        Set<String> set = zset.reverseRange(key, 0, 2);// board_id 얻어옴
        Map<Map<String, String>, List<PostDto>> resultMap = new HashMap<>();
        for (String board_id : set) {
            // db조회 객체 top3 얻음
            Map<String, String> boardInfo = boardService.getBoardInfo(board_id);
            if (boardInfo != null) {// null이 아닐때만 랭크에 넣어줌
                resultMap.put(boardInfo, sqlSession.getMapper(MainMapper.class).getRecentFive(board_id));
            }
        }
        // boardPostRank캐시에 넣기
        ObjectMapper mapper = new ObjectMapper();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        try {
            String msg = mapper.writeValueAsString(resultMap);
            // logger.info("캐시:{}", msg);
            valueOps.set("boardPostRank", msg);
        } catch (JsonProcessingException e) {
            logger.error("updatePostSort", e);
        }
    }

    @Scheduled(fixedDelay = 60000) // 1분 마다 캐시 갱신
    @Override
    public void updatePostLikeSort() {
        logger.info("postLikeRank, postCommentRank 캐시 업데이트");
        String key = "postLikeSort";
        String key2 = "postCommentSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        // 좋아요 top3
        Set<String> set = zset.reverseRange(key, 0, 2);// board_id 얻어옴
        // 댓글수 top3
        Set<String> commentSet = zset.reverseRange(key2, 0, 2);
        List<PostDto> postLikeList = new LinkedList<>();
        List<Map<String, String>> postCommList = new LinkedList<>();
        for (String post_id : set) {
            // db조회 객체 top3 얻음
            postLikeList.add(postService.getPostById(Integer.valueOf(post_id)));
        }
        for (String post_id : commentSet) {
            // db조회 객체 top3 얻음
            Map<String, String> postmap = new HashMap<>();
            PostDto postDto = postService.getPostById(Integer.valueOf(post_id));
            if (postDto != null) {
                postmap.put("post_id", String.valueOf(postDto.getPost_id()));
                postmap.put("post_title", postDto.getPost_title());
                postmap.put("board_id", String.valueOf(postDto.getBoard_id()));
                postmap.put("post_img", postDto.getPost_image());
                postmap.put("comment_count", String.valueOf(Math.round(zset.score("postCommentSort", post_id))));
                postCommList.add(postmap);
            }
        }
        // boardPostRank캐시에 넣기
        ObjectMapper mapper = new ObjectMapper();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        try {
            String msg = mapper.writeValueAsString(postLikeList);
            String msg2 = mapper.writeValueAsString(postCommList);
            // logger.info("캐시:{}", msg);
            // postLikeRank 키 값으로 매핑
            valueOps.set("postLikeRank", msg);
            valueOps.set("postCommentRank", msg2);
        } catch (JsonProcessingException e) {
            logger.error("updatePostLikeSor", e);
        }
    }

}
