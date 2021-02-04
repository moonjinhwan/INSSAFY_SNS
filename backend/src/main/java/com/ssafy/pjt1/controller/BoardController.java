package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.service.BoardService;
import com.ssafy.pjt1.model.service.main.MainService;
import com.ssafy.pjt1.model.service.post.PostService;
import com.ssafy.pjt1.model.service.vote.VoteService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/board")
public class BoardController {

    public static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String PERMISSION = "No Permission";

    @Autowired
    private BoardService boardService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private PostService postService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MainService mainService;

    /*
     * 기능: 보드 생성
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_name, board_description, board_location,
     * board_igmyeong, board_hash, checklist_flag, calendar_flag, vote_flag
     * 
     * @return : message, board_id
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> boardCreate(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/create 호출성공");
        try {
            BoardDto boardDto = new BoardDto();
            boardDto.setBoard_name((String) param.get("board_name"));
            boardDto.setBoard_description((String) param.get("board_description"));
            boardDto.setBoard_image((String) param.get("board_image"));
            boardDto.setBoard_igmyeong((int) param.get("board_igmyeong"));
            boardDto.setBoard_location((String) param.get("board_location"));
            boardDto.setBoard_hash((String) param.get("board_hash"));
            boardDto.setUser_id((String) param.get("user_id"));
            boardService.createBoard(boardDto);

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", boardDto.getUser_id());
            map.put("board_id", boardDto.getBoard_id());
            map.put("user_role", 1);
            boardService.subscribe(map);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("board_id", boardDto.getBoard_id());
            map2.put("checklist_flag", (int) param.get("checklist_flag"));
            map2.put("calendar_flag", (int) param.get("calendar_flag"));
            map2.put("vote_flag", (int) param.get("vote_flag"));
            boardService.addFunction(map2);

            resultMap.put("board_id", boardDto.getBoard_id());
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 구독
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id, user_role
     * 
     * @return : message
     */
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/subscribe 호출성공");
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", (String) param.get("user_id"));
            map.put("board_id", (int) param.get("board_id"));
            map.put("user_role", (int) param.get("user_role"));

            String board_id = String.valueOf(param.get("board_id"));
            int count = boardService.isSubscribed(map);
            if (count == 0) {
                logger.info("구독 설정");
                boardService.subscribe(map);
                // 구독 누르면 캐시에 해당 보드 구독한 수 넣기
                zset.add("follow", board_id, mainService.getSubsriptionNumber(board_id) + 1);
            } else {
                int count2 = boardService.isUnSubscribed(map);
                if (count2 == 0) {
                    // 전에 구독한 이력이 있지만 현재는 아닌 경우
                    boardService.updateSubscribe(map);
                    // 구독 누르면 캐시에 해당 보드 구독한 수 넣기
                    zset.add("follow", board_id, mainService.getSubsriptionNumber(board_id) + 1);
                } else {
                    logger.info("구독 해지");
                    // 관리자 아닐 경우 구독 해지
                    boardService.unsubscribe(map);
                }
            }

            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 회원 검색
     * 
     * developer: 윤수민
     * 
     * @param : keyword
     * 
     * @return : message
     */
    @GetMapping("/searchUser/{keyword}")
    public ResponseEntity<Map<String, Object>> searchUser(@PathVariable("keyword") String keyword) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/searchUser 호출성공");
        try {
            resultMap.put("message", SUCCESS);
            List<UserDto> userList = boardService.searchUser(keyword);
            resultMap.put("userList", userList);

        } catch (Exception e) {
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 관리자 추가
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id
     * 
     * @return : message
     */
    @PostMapping("/updateManager")
    public ResponseEntity<Map<String, Object>> updateManager(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/updateManager 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", (String) param.get("user_id"));
            map.put("board_id", (int) param.get("board_id"));
            int count = boardService.isSubscribed(map);
            if (count == 0) {
                logger.info("구독 설정 + 관리자 추가");
                map.put("user_role", 1);
                boardService.subscribe(map);
            } else {
                logger.info("관리자 추가");
                boardService.updateManager(map);
            }

            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 수정
     * 
     * developer: 윤수민
     * 
     * @param : BoardDto, login_id
     * 
     * @return : message
     */
    @PutMapping("/modify")
    public ResponseEntity<Map<String, Object>> modifyBoard(@RequestBody BoardDto boardDto,
    @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/modify 호출 성공");
        try {
            int board_id = boardDto.getBoard_id();
            Map<String, Object> map = new HashMap<>();
            map.put("board_id",board_id);
            map.put("login_id", login_id);
            if(boardService.isManager(map)!=0){
                if (boardService.modifyBoard(boardDto) == 1) {
                    resultMap.put("message", SUCCESS);
                } else {
                    resultMap.put("message", FAIL);
                }
            }else{
                resultMap.put("message", PERMISSION);
            }   
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("수정 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 전체 보드 (최신순, 인기순)
     * 
     * developer: 윤수민
     * 
     * @param : sort
     * 
     * @return : boardList, message
     */
    @GetMapping("/getBoards")
    public ResponseEntity<Map<String, Object>> getBoards(@RequestParam(value = "sort") String sort) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/board/getBoards 호출 성공");

        try {
            List<BoardDto> boardList;
            if (sort.equals("new")) {
                logger.info("최신순 전체 보드 검색");
                boardList = boardService.getBoardsNew();
            } else {
                logger.info("구독순 전체 보드 검색");
                boardList = boardService.getBoardsPopular();
            }
            resultMap.put("boardList", boardList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("목록 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 검색 (최신순, 인기순)
     * 
     * developer: 윤수민
     * 
     * @param : sort, keyword
     * 
     * @return : boardList, message
     */
    @GetMapping("/searchBoard")
    public ResponseEntity<Map<String, Object>> searchBoard(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "keyword") String keyword) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/board/searchBoard 호출 성공");
        try {
            List<BoardDto> boardList;
            if (sort.equals("new")) {
                logger.info("최신순 보드 검색");
                boardList = boardService.searchBoardNew(keyword);
            } else {
                logger.info("구독순 보드 검색");
                boardList = boardService.searchBoardPopular(keyword);
            }
            resultMap.put("boardList", boardList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("검색 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 삭제
     * 
     * developer: 윤수민
     * 
     * @param : board_id, login_id
     * 
     * @return : message
     */
    @DeleteMapping("/delete/{board_id}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("board_id") int board_id,
    @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/delete 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("board_id",board_id);
            map.put("login_id", login_id);
            if(boardService.isManager(map)!=0){
                if (boardService.deleteBoard(board_id) == 1) {
                    // 추가기능 is_used 0으로 변경
                    boardService.deleteBoardAll(board_id);
                    boardService.deleteCalendar(board_id);
                    boardService.deleteCheckList(board_id);
                    boardService.deleteVote(board_id);
                    List<Integer> voteList = boardService.getVoteList(board_id);
                    for (Integer vote_id : voteList) {
                        voteService.voteDeleteAll(vote_id);
                    }
                    // 구독 is_used 0으로 변경
                    boardService.deleteSubscription(board_id);
                    // 포스트 is_used 0으로 변경
                    List<Integer> postList = boardService.getPostList(board_id);
                    for (Integer post_id : postList) {
                        if (postService.postDelete(post_id) == 1) {
                            // postService.deleteScrapAll(post_id);
                            // postService.deleteLikeAll(post_id);
                            // postService.deleteCommentAll(post_id);
                            resultMap.put("message", SUCCESS);
                        }
                    }
                    boardService.deletePostAll(board_id);
                    resultMap.put("message", SUCCESS);
                }
            }else{
                resultMap.put("message", PERMISSION);
            }
            
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("error", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 디테일
     * 
     * developer: 윤수민
     * 
     * @param : board_id
     * 
     * @return : message
     */
    @GetMapping("/detail")
    public ResponseEntity<Map<String, Object>> detailBoard(@RequestParam(value = "board_id") int board_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/searchUser 호출성공");
        try {
            BoardDto boardDto = boardService.detailBoard(board_id);
            if (boardDto != null) {
                int board_count = boardService.getBoardCount(board_id);
                resultMap.put("boardDto", boardDto);
                resultMap.put("board_count", board_count);
                resultMap.put("message", SUCCESS);
            }else{
                resultMap.put("message", "NULL");
            }
            
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
