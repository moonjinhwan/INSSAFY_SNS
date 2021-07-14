package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ssafy.pjt1.model.service.post.PostService;
import com.ssafy.pjt1.model.service.redis.RedisService;
import com.ssafy.pjt1.model.service.vote.VoteService;

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
    private RedisService redisService;

    /*
     * 기능: 보드 생성
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_name, board_description, board_location,
     * board_igmyeong, board_hash, checklist_flag, calendar_flag, vote_flag,
     * board_state
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
            boardDto.setBoard_state((int) param.get("board_state"));
            boardService.createBoard(boardDto);

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", boardDto.getUser_id());
            map.put("board_id", boardDto.getBoard_id());
            map.put("user_role", 1);
            boardService.subscribe(map);
            /////////////////////////////////////////////////// 구독 누르면 캐시에 해당 보드 구독한 수 넣기
            redisService.boardFollowSortSet(String.valueOf(boardDto.getBoard_id()));

            Map<String, Object> map2 = new HashMap<>();
            map2.put("board_id", boardDto.getBoard_id());
            map2.put("checklist_flag", (int) param.get("checklist_flag"));
            map2.put("calendar_flag", (int) param.get("calendar_flag"));
            map2.put("vote_flag", (int) param.get("vote_flag"));
            map2.put("user_rank_flag", (int) param.get("user_rank_flag"));
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
                /////////////////////////////////////////////////// 구독 누르면 캐시에 해당 보드 구독한 수 넣기
                redisService.boardFollowSortSet(board_id);
                resultMap.put("message", SUCCESS);
            } else {
                int count2 = boardService.isUnSubscribed(map);
                if (count2 == 0) {
                    // 전에 구독한 이력이 있지만 현재는 아닌 경우
                    boardService.updateSubscribe(map);
                    /////////////////////////////////////////////// 구독 누르면 캐시에 해당 보드 구독한 수 넣기
                    redisService.boardFollowSortSet(board_id);
                    resultMap.put("message", SUCCESS);
                } else {
                    logger.info("구독 해지");
                    // 관리자 아닐 경우 구독 해지
                    boardService.unsubscribe(map);
                    ///////////////////////////////////////////// 구독 해지시 redis에서 follower수 -1 감소
                    redisService.boardFollowSortSetDecrease(String.valueOf(board_id));
                    resultMap.put("message", SUCCESS);
                }
            }

            if ((int) param.get("user_role") == 1) {
                resultMap.put("message", "관리자는 구독취소 불가");
            }

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
                /////////////////////////////////////////////////// 구독 누르면 캐시에 해당 보드 구독한 수 넣기
                redisService.boardFollowSortSet((String) param.get("board_id"));
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
            map.put("board_id", board_id);
            map.put("login_id", login_id);
            if (boardService.isManager(map) != 0) {
                if (boardService.modifyBoard(boardDto) == 1) {
                    resultMap.put("message", SUCCESS);
                } else {
                    resultMap.put("message", FAIL);
                }
            } else {
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
     * @param : sort, page, size
     * 
     * @return : boardList, message, isLastPage
     */
    @GetMapping("/getBoards")
    public ResponseEntity<Map<String, Object>> getBoards(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/board/getBoards 호출 성공");

        try {
            int totalCnt = boardService.getTotalCnt();
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");

            Map<String, Object> map = new HashMap<>();
            map.put("start", page * size);
            map.put("size", size);
            List<Map<String, Object>> boardList;
            if (sort.equals("new")) {
                logger.info("최신순 전체 보드 검색");
                boardList = boardService.getBoardsNew(map);
            } else {
                logger.info("구독순 전체 보드 검색");
                boardList = boardService.getBoardsPopular(map);
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
     * 기능: 보드 검색 (최신순, 인기순, 타입별)
     * 
     * developer: 윤수민
     * 
     * @param : sort, keyword, page, size, type(name, description, location, hash)
     * 
     * @return : boardList, message, isLastPage
     */
    @GetMapping("/searchBoard")
    public ResponseEntity<Map<String, Object>> searchBoard(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size, @RequestParam(value = "type") String type) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/board/searchBoard 호출 성공");
        try {
            int totalCnt;
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", keyword);
            map.put("start", page * size);
            map.put("size", size);
            List<Map<String, Object>> boardList;
            if (sort.equals("new")) {
                if (type.equals("name")) {
                    logger.info("최신순 보드 이름 검색");
                    totalCnt = boardService.getSearchCntN(keyword);
                    boardList = boardService.searchBoardNewN(map);
                } else if (type.equals("description")) {
                    logger.info("최신순 보드 내용 검색");
                    totalCnt = boardService.getSearchCntD(keyword);
                    boardList = boardService.searchBoardNewD(map);
                } else if (type.equals("location")) {
                    logger.info("최신순 보드 지역 검색");
                    totalCnt = boardService.getSearchCntL(keyword);
                    boardList = boardService.searchBoardNewL(map);
                } else {
                    logger.info("최신순 보드 해쉬 검색");
                    totalCnt = boardService.getSearchCntH(keyword);
                    boardList = boardService.searchBoardNewH(map);
                }
                // boardList = boardService.searchBoardNew(map);
            } else {
                if (type.equals("name")) {
                    logger.info("구독순 보드 이름 검색");
                    totalCnt = boardService.getSearchCntN(keyword);
                    boardList = boardService.searchBoardPopularN(map);
                } else if (type.equals("description")) {
                    logger.info("구독순 보드 내용 검색");
                    totalCnt = boardService.getSearchCntD(keyword);
                    boardList = boardService.searchBoardPopularD(map);
                } else if (type.equals("location")) {
                    logger.info("구독순 보드 지역 검색");
                    totalCnt = boardService.getSearchCntL(keyword);
                    boardList = boardService.searchBoardPopularL(map);
                } else {
                    logger.info("구독순 보드 해쉬 검색");
                    totalCnt = boardService.getSearchCntH(keyword);
                    boardList = boardService.searchBoardPopularH(map);
                }
                // boardList = boardService.searchBoardPopular(map);
            }
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");
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
            map.put("board_id", board_id);
            map.put("login_id", login_id);
            if (boardService.isManager(map) != 0) {
                if (boardService.deleteBoard(board_id) == 1) {
                    //////////////////////////////// 보드 삭제시 redis에서도 삭제
                    redisService.boardSortSetDelete(String.valueOf(board_id));
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
            } else {
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
                Map<String, Object> map = new HashMap<>();
                map = boardService.boardFunc(board_id);
                resultMap.put("boardDto", boardDto);
                resultMap.put("board_count", board_count);
                resultMap.put("board_function", map);

                resultMap.put("message", SUCCESS);
            } else {
                resultMap.put("message", "NULL");
            }

        } catch (Exception e) {
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 추가 기능 수정
     * 
     * developer: 윤수민
     * 
     * @param : board_id, function, login_id, option( 0: 삭제, 1: 추가)
     * 
     * @return : message
     */
    @PutMapping("/modifyFunction")
    public ResponseEntity<Map<String, Object>> modifyFunction(@RequestParam(value = "board_id") int board_id,
            @RequestParam(value = "function") String function, @RequestParam(value = "login_id") String login_id,
            @RequestParam(value = "option") int option) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/board/modifyFunction 호출 성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("board_id", board_id);
            map.put("login_id", login_id);
            if (boardService.isManager(map) != 0) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("board_id", board_id);
                map2.put("option", option);
                if (function.equals("checklist")) {
                    boardService.addChecklist(map2);
                } else if (function.equals("calendar")) {
                    boardService.addCalendar(map2);
                } else if (function.equals("vote")) {
                    boardService.addVote(map2);
                } else if (function.equals("userRank")) {
                    boardService.addUserRank(map2);
                }
                resultMap.put("message", SUCCESS);
            } else {
                resultMap.put("message", PERMISSION);
            }
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("수정 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

}
