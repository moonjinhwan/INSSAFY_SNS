package com.ssafy.pjt1.model.service.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt1.model.dto.post.PostDto;
import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.service.UserService;
import com.ssafy.pjt1.model.service.comment.CommentService;
import com.ssafy.pjt1.model.service.post.PostService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    /*
     * 기능: 회원삭제시 관련된 보드 삭제
     * 
     * developer: 문진환
     */
    public void userDelete(String user_id) {
        // 회원 삭제할때
    }

    /*
     * 기능: 구독삭제 시 redis sort set안에 게사판별 follow수 -1 감소
     * 
     * developer: 문진환
     */
    public void boardFollowSortSetDecrease(String board_id) {
        // board_id를 key값으로 follow수 얻어오기
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String sortkey = "boardFollowSort";
        zset.incrementScore(sortkey, board_id, -1);
    }

    /*
     * 기능: 보드삭제 시 redis sort set안에 게시판 삭제
     * 
     * developer: 문진환
     */
    public void boardSortSetDelete(String board_id) {
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String boardFollow = "boardFollowSort";
        String boardPost = "boardPostSort";
        // sort set에서 삭제
        zset.remove(boardFollow, board_id);
        zset.remove(boardPost, board_id);
    }

    /*
     * 기능: post 삭제 시 redis sort set안에 -1 감소
     * 
     * developer: 문진환
     */
    public void boardPostSortSetDecrease(int post_id) {
        // board_id를 key값으로 follow수 얻어오기
        PostDto postDto = postService.getPostById(post_id);
        int board_id = postDto.getBoard_id();
        // post_id를 활용해서 board_id 얻음
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String sortkey = "boardPostSort";
        zset.incrementScore(sortkey, String.valueOf(board_id), -1);
    }

    /*
     * 기능: 구독누를 시 redis sort set안에 게사판별 follow수 업데이트
     * 
     * developer: 문진환
     */
    public void boardFollowSortSet(String board_id) {
        // board_id를 key값으로 follow수 얻어오기
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String sortkey = "boardFollowSort";
        zset.incrementScore(sortkey, board_id, 1);
    }

    /*
     * 기능: post 작성 시 redis sort set안에 게시판별 post수 업데이트
     * 
     * developer: 문진환
     */
    public void boardPostSortSet(String board_id) {
        String sortkey = "boardPostSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        // post개수 받고 1증가해서 저장
        zset.incrementScore(sortkey, board_id, 1);
    }

    /*
     * 기능: 좋아요 redis sort set 1 증가
     * 
     * developer: 문진환
     */
    public void PostLikeSortSet(int post_id) {
        String sortkey = "postLikeSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        // post개수 받고 1증가해서 저장
        zset.incrementScore(sortkey, String.valueOf(post_id), 1);
    }

    /*
     * 기능: 싫어요 redis sort set 1 감소
     * 
     * developer: 문진환
     */
    public void postLikeDecrease(int post_id) {
        String sortkey = "postLikeSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.incrementScore(sortkey, String.valueOf(post_id), -1);
    }

    /*
     * 기능: 댓글 달시 sort set 1 증가
     * 
     * developer: 문진환
     */
    public void postCommentSortset(int post_id) {
        String sortkey = "postCommentSort";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.incrementScore(sortkey, String.valueOf(post_id), 1);
    }

    /*
     * 기능: 댓글 삭제 sort set 1 감소
     * 
     * developer: 문진환
     */
    public void postCommentSortsetDecrease(int comment_id) {
        String sortkey = "postCommentSort";
        String post_id = commentService.getPostIdByCommentId(comment_id);
        // logger.info(">>>>>>>>>>>>>>>>post_id:{}", post_id);
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.incrementScore(sortkey, post_id, -1);
    }

    public void increaseUserRank(int post_id, int board_id) {
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String boardId = String.valueOf(board_id);
        String key = "func:" + "userRank:" + boardId;
        PostDto post = postService.getPostById(post_id);
        zset.incrementScore(key, post.getUser_id(), 1);
    }

    // 싫어요 눌러서 유저랭킹 감소
    public void decreaseUserRank(int post_id, int board_id) {
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        String boardId = String.valueOf(board_id);
        String key = "func:" + "userRank:" + boardId;
        PostDto post = postService.getPostById(post_id);
        zset.incrementScore(key, post.getUser_id(), -1);
    }

    // 1분마다 랭킹 update - 유저랭킹, 게시물 랭킹
    @Scheduled(fixedDelay = 60000)
    public void updateAddFuncRank() throws JsonProcessingException {
        log.info("추가 기능 Redis update(1분)");
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        Set<String> keys = redisTemplate.keys("func:userRank:*");
        valOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        // 게시판 마다 돌리기
        for (String key : keys) {
            // top3 user갖고 오기

            List<Map<String, String>> list = new ArrayList<>();
            Set<String> top3Id = zset.reverseRange(key, 0, 2);
            // 아이디로 객체 갖고오기
            if (top3Id != null) {
                for (String id : top3Id) {
                    UserDto dto = userService.userDtoById(id);
                    if(dto==null)
                        continue;
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("nickName", dto.getUser_nickname());
                    userMap.put("score", Math.round(zset.score(key, id)) + "");
                    list.add(userMap);
                }
                String listString = mapper.writeValueAsString(list);
                valOps.set("sortSet:" + key, listString);
            }
        }
    }

    /*
     * 기능: post 작성 시 redis techStack + 1
     * 
     * developer: 윤수민
     */
    public void postTechStackPlus(String post_header) {
        post_header = post_header.replace("#", "");
        post_header = post_header.replace("|", "#");
        String[] list = post_header.split("#");
        String sortkey = "techStack";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        for (String techStack_name : list) {
            if (!techStack_name.equals("null")) {
                zset.incrementScore(sortkey, String.valueOf(techStack_name), 1);
            }
        }
    }

    /*
     * 기능: post삭제 시 redis techStack -1 감소
     * 
     * developer: 윤수민
     */
    public void postTechStackMinus(String post_header) {
        post_header = post_header.replace("#", "");
        post_header = post_header.replace("|", "#");
        String[] list = post_header.split("#");
        String sortkey = "techStack";
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        for (String techStack_name : list) {
            if (!techStack_name.equals("null")) {
                zset.incrementScore(sortkey, String.valueOf(techStack_name), -1);
            }
        }
    }

    /*
     * 기능: 기술스택 데이터 리스트
     * 
     * developer: 윤수민
     */
    public List<Map<String, Object>> getWordData() {
        String key = "techStack";
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<String> range = zSetOps.range(key, 0, 33);
        Iterator<String> iter = range.iterator();
        List<Map<String, Object>> list = new ArrayList<>(range.size());
        while (iter.hasNext()) {
            String value = iter.next();
            Map<String, Object> map = new HashMap<>();
            Double d = zSetOps.score(key, value);
            int score = Integer.parseInt(String.valueOf(Math.round(d)));
            map.put("name", value);
            map.put("score", score);
            list.add(map);
        }
        return list;
    }

}
