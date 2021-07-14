package com.ssafy.pjt1.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.bamboo.BambooDto;
import com.ssafy.pjt1.model.service.bamboo.BambooService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/bamboo")
public class BambooController {

    public static final Logger logger = LoggerFactory.getLogger(BambooController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String PERMISSION = "No Permission";


    @Autowired
    private BambooService bambooService;

    /*
     * 기능: 대나무숲 포스트 작성
     * 
     * developer: 윤수민
     * 
     * @param : user_id, bamboo_title, bamboo_description, bamboo_image,
     * bamboo_iframe, bamboo_header, writer_nickname
     * 
     * @return : message, bamboo_id
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> postCreate(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/create 호출성공");
        try {
            BambooDto bambooDto = new BambooDto();
            bambooDto.setBamboo_description((String) param.get("bamboo_description"));
            bambooDto.setBamboo_header((String) param.get("bamboo_header"));
            bambooDto.setBamboo_iframe((String) param.get("bamboo_iframe"));
            bambooDto.setBamboo_image((String) param.get("bamboo_image"));
            bambooDto.setBamboo_title((String) param.get("bamboo_title"));
            bambooDto.setUser_id((String) param.get("user_id"));
            bambooDto.setWriter_nickname((String) param.get("writer_nickname"));
            bambooDto.setBamboo_image((String) param.get("bamboo_image"));
            bambooService.createBamboo(bambooDto);
            resultMap.put("bamboo_id", bambooDto.getBamboo_id());
            resultMap.put("message", SUCCESS);

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 대나무숲 포스트 리스트
     * 
     * developer: 윤수민
     * 
     * @param : page, size
     * 
     * @return : message, isLastPage(false: 마지막 페이지 아님, true: 마지막 페이지, No data: 출력할 데이터 없음)
     * bambooList(bamboo_title, bamboo_description, bamboo_image,
     * bamboo_iframe, bamboo_header, writer_nickname)
     */
    @GetMapping("/getAllList")
    public ResponseEntity<Map<String, Object>> getPostByList(@RequestParam(value = "page") int page,
    @RequestParam(value = "size") int size) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/getAllList 호출성공");
        try {
            int totalCnt = bambooService.getTotalCnt();
            if(totalCnt>(page+1)*size){
                resultMap.put("isLastPage","false");
            }else if(totalCnt>page*size){
                resultMap.put("isLastPage","true");
            }else{
                resultMap.put("isLastPage","No data");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("start",page*size);
            map.put("size",size);
            List<BambooDto> bambooList = bambooService.getAllList(map);
            resultMap.put("bambooList", bambooList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 대나무숲 포스트 검색 (최신순)
     * 
     * developer: 윤수민
     * 
     * @param : keyword, page, size, type(title, description)
     * 
     * @return : bambooList, message, isLastPage
     */
    @GetMapping("/searchPost")
    public ResponseEntity<Map<String, Object>> searchPost(@RequestParam(value = "keyword") String keyword,
    @RequestParam(value = "page") int page,@RequestParam(value = "size") int size
    , @RequestParam(value = "type") String type) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/bamboo/searchPost 호출 성공");
        try {
            int totalCnt;
            Map<String, Object> map = new HashMap<>();
            map.put("start",page*size);
            map.put("size",size);
            map.put("keyword",keyword);
            List<BambooDto> bambooList = new ArrayList<>();
            if(type.equals("title")){
                logger.info("최신순 대나무숲 타이틀 검색");
                totalCnt = bambooService.getSearchCntT(keyword);
                bambooList = bambooService.searchPostT(map);
            }else{
                logger.info("최신순 대나무숲 내용 검색");
                totalCnt = bambooService.getSearchCntD(keyword);
                bambooList = bambooService.searchPostD(map);
            }
            // bambooList = bambooService.searchPost(map);
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");
            resultMap.put("bambooList", bambooList);
            if(!bambooList.isEmpty()){
                resultMap.put("message", SUCCESS);
            }else{
                resultMap.put("message", "NULL");
            }
                        
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("검색 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 대나무숲 포스트 수정
     * 
     * developer: 윤수민
     * 
     * @param : BambooDto, login_id
     * 
     * @return : message
     */
    @PutMapping("/modify")
    public ResponseEntity<Map<String, Object>> bambooModify(@RequestBody BambooDto bambooDto,
            @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/modify 호출 성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("bamboo_id", bambooDto.getBamboo_id());
            map.put("login_id", login_id);
            if (bambooService.isWriter(map) != 0) {
                if (bambooService.bambooModify(bambooDto) == 1) {
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
     * 기능: 대나무숲 포스트 삭제
     * 
     * developer: 윤수민
     * 
     * @param : bamboo_id, login_id
     * 
     * @return : message
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> bamboooDelete(@RequestParam(value = "bamboo_id") int bamboo_id,
    @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/delete 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("bamboo_id", bamboo_id);
            map.put("login_id", login_id);
            if (bambooService.isWriter(map) != 0) {
                if (bambooService.bambooDelete(bamboo_id) == 1) {
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

}