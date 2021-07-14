package com.ssafy.pjt1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.service.BoardService;
import com.ssafy.pjt1.model.service.post.PostService;
import com.ssafy.pjt1.model.service.redis.RedisService;
import com.ssafy.pjt1.model.service.study.StudyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/study")
public class StudyController {

    public static final Logger logger = LoggerFactory.getLogger(StudyController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String PERMISSION = "No Permission";

    @Autowired
    private StudyService studyService;

    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;

    @Autowired
	private RedisService redisService;

    /*
     * 기능: 스터디 모집글 리스트
     * 
     * developer: 윤수민
     * 
     * @param : page, size
     * 
     * @return : message, 
     * postList(post_id,user_id,post_date,post_title,post_description,
     * post_image,post_iframe,post_header,post_state,like_count, comment_count)
     */
    @GetMapping("/getPromoList")
    public ResponseEntity<Map<String, Object>> getPromoList(@RequestParam(value = "page") int page, 
    @RequestParam(value = "size") int size) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/getPostList 호출성공");
        try {
            int totalCnt = postService.getTotalPostCnt(77);
            if(totalCnt>(page+1)*size) resultMap.put("isLastPage","false");
            else if(totalCnt>page*size) resultMap.put("isLastPage","true");
            else resultMap.put("isLastPage","No data");

            Map<String, Object> map = new HashMap<>();
            map.put("start", page*size);
            map.put("size", size);
            List<Map<String, Object>> postList = studyService.getPromoList(map);
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 내 스터디 리스트
     * 
     * developer: 윤수민
     * 
     * @param : login_id
     * 
     * @return : message, studyList(board_name,board_description,board_count)
     * 
     */
    @GetMapping("/getStudyList")
    public ResponseEntity<Map<String, Object>> getStudyList(@RequestParam(value = "login_id") String login_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/getStudyList 호출성공");
        try {
            List<Integer> studyIdList = studyService.getStudyId(login_id); 
            List<Map<String, Object>> studyList = new ArrayList<Map<String, Object>>();
            for (Integer board_id : studyIdList) {
                studyList.add(studyService.getStudyInfo(board_id)); 
            }
            resultMap.put("studyList", studyList);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 모든 스터디 리스트(생성순)
     * 
     * developer: 윤수민
     * 
     * @param : page, size
     * 
     * @return : message, isLastPage, studyList(board_name,board_description,board_count)
     * 
     */
    @GetMapping("/getAllList")
    public ResponseEntity<Map<String, Object>> getAllList(@RequestParam(value = "page") int page, 
    @RequestParam(value = "size") int size){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/getAllList 호출성공");
        try {
            int totalCnt = studyService.getTotalCnt();
            if(totalCnt>(page+1)*size){
                resultMap.put("isLastPage","false");
            }else if(totalCnt>page*size){
                resultMap.put("isLastPage","true");
            }else{
                resultMap.put("isLastPage","No data");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("start", page*size);
            map.put("size", size);
            List<Map<String, Object>> studyList = studyService.getAllList(map);
            resultMap.put("studyList", studyList);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 워드클라우드 데이터
     * 
     * developer: 윤수민
     * 
     * @param : 
     * 
     * @return : message, defaultWords
     * 
     */
    @GetMapping("/wordCloud")
    public ResponseEntity<Map<String, Object>> wordCloud(){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/wordCloud 호출성공");
        try {
            List<Map<String, Object>> defaultWords = redisService.getWordData();
            resultMap.put("defaultWords", defaultWords);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 스터디 가입 요청
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id
     * 
     * @return : message
     */
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> studyRequest(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/request 호출성공");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", (String) param.get("user_id"));
            map.put("board_id", (int) param.get("board_id"));
            if(boardService.isSubscribed(map)>0){
                // 전에 가입했다가 탈퇴한 스터디 그룹 재가입하는 경우
                studyService.reSubscription(map);
                boardService.updateSubscribe(map);
            }else{
                studyService.request(map);
            }
            resultMap.put("message", SUCCESS);

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 스터디 가입 요청 리스트
     * 
     * developer: 윤수민
     * 
     * @param : board_id
     * 
     * @return : message, list
     * 
     */
    @GetMapping("/requestList")
    public ResponseEntity<Map<String, Object>> getRequestList(@RequestParam(value = "board_id") int board_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/requestList 호출성공");
        try {
            List<Map<String, Object>> list = studyService.getRequestList(board_id);
            resultMap.put("list", list);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 스터디 가입 수락/거절
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id, option(-1이면 거절, 1이면 수락)
     * 
     * @return : message
     */
    @PostMapping("/requestProcess")
    public ResponseEntity<Map<String, Object>> studyProcess(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/requestProcess 호출성공");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", (String) param.get("user_id"));
            map.put("board_id", (int) param.get("board_id"));
            map.put("option", (int) param.get("option"));
            if((int) param.get("option") == 1){
                studyService.requestProcess(map);
                // 수락하는 경우 구독테이블에 추가
                map.put("user_role", 0);
                boardService.subscribe(map);
            }else{
                studyService.requestProcess(map);
            }
            resultMap.put("message", SUCCESS);

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 스터디 그룹 탈퇴
     * 
     * developer: 윤수민
     * 
     * @param : board_id, user_id
     * 
     * @return : message
     */
    @DeleteMapping("/secession")
    public ResponseEntity<Map<String, Object>> secession(@RequestParam(value = "board_id") int board_id,
            @RequestParam(value = "user_id") String user_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/secession 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("board_id", board_id);
            map.put("user_id", user_id);
            studyService.secession(map);
            // 탈퇴하는 경우 구독테이블에서도 삭제처리
            boardService.unsubscribe(map);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("error", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 내 스터디 가입 요청 현황
     * 
     * developer: 윤수민
     * 
     * @param : user_id
     * 
     * @return : message, list
     * 
     */
    @GetMapping("/myRequestList")
    public ResponseEntity<Map<String, Object>> myRequestList(@RequestParam(value = "user_id") String user_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/myRequestList 호출성공");
        try {
            List<Map<String, Object>> list = studyService.myRequestList(user_id);
            resultMap.put("list", list);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 특정 스터디 가입 요청 현황
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id
     * 
     * @return : message, list
     * 
     */
    @GetMapping("/myStudyRequest")
    public ResponseEntity<Map<String, Object>> myStudyRequest(@RequestParam(value = "user_id") String user_id,
    @RequestParam(value = "board_id") int board_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("study/myRequestList 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("board_id", board_id);
            map.put("user_id", user_id);
            List<Map<String, Object>> list = studyService.myStudyRequest(map);
            resultMap.put("list", list);          
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

}
