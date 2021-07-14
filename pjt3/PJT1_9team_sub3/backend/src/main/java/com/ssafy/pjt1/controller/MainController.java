package com.ssafy.pjt1.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt1.model.dto.board.BoardDto;
import com.ssafy.pjt1.model.dto.post.PostDto;
import com.ssafy.pjt1.model.service.S3Service;
import com.ssafy.pjt1.model.service.main.MainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/main")
public class MainController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Autowired
    private MainService service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private S3Service s3Service;

    @ApiOperation(value = "즐겨찾기 리스트 불러오기")
    @GetMapping("/selectFavorite/{user_id}")
    public ResponseEntity<Map<String, Object>> selectFavorite(@PathVariable("user_id") String user_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            List<BoardDto> list = service.selectFavorite(user_id);
            resultMap.put("favorite", list);
            resultMap.put("status", SUCCESS);
        } catch (Exception e) {
            logger.error("error", e);
            resultMap.put("status", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "팔로우 수 top3 불러오기")
    @GetMapping("/getFollowRank")
    public ResponseEntity<Map<Map<String, String>, List<PostDto>>> getFollowRank() {
        Map<Map<String, String>, List<PostDto>> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // top3만 갖고오기
            // logger.info("top{}", valueOps.get("followRank"));
            resultMap = mapper.readValue(valueOps.get("boardFollowRank"), Map.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return new ResponseEntity<Map<Map<String, String>, List<PostDto>>>(resultMap, status);
    }

    @ApiOperation(value = "게시글 수 top3 불러오기")
    @GetMapping("/getBoardPostRank")
    public ResponseEntity<Map<Map<String, String>, List<PostDto>>> getPosttRank() {
        Map<Map<String, String>, List<PostDto>> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // top3만 갖고오기
            // logger.info("top{}", valueOps.get("followRank"));
            resultMap = mapper.readValue(valueOps.get("boardPostRank"), Map.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return new ResponseEntity<Map<Map<String, String>, List<PostDto>>>(resultMap, status);
    }

    @ApiOperation(value = "좋아요 top3 불러오기")
    @GetMapping(value = "/getPostLikeRank")
    public ResponseEntity<Map<String, List<PostDto>>> getLikeRank() {
        Map<String, List<PostDto>> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PostDto> postDto = mapper.readValue(valueOps.get("postLikeRank"), List.class);
            resultMap.put("like", postDto);
            // resultMap = mapper.readValue(valueOps.get("postLikeRank"), List.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return new ResponseEntity<Map<String, List<PostDto>>>(resultMap, status);
    }

    /*
     * 기능: top3 코멘트 랭킹
     * 
     * developer: 문진환
     *
     */
    @ApiOperation(value = "코멘트 수 기준 post top3 불러오기")
    @GetMapping(value = "/getCommentRank")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getCommentRank() {
        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        logger.info(">>>>>>>>>>>>>>>>>getCommentRank");
        try {
            List<Map<String, String>> postDto = mapper.readValue(valueOps.get("postCommentRank"), List.class);
            resultMap.put("postComment", postDto);
            // resultMap = mapper.readValue(valueOps.get("postLikeRank"), List.class);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return new ResponseEntity<Map<String, List<Map<String, String>>>>(resultMap, status);
    }

    /*
     * 기능: 이미지 삽입
     * 
     * developer: 윤수민
     * 
     * @param : file
     * 
     * @return : message, imgPath
     */
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> imageUpload(MultipartFile file) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/image 호출성공");
        try {
            String imgPath = s3Service.upload(file);
            resultMap.put("imgPath", imgPath);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 이미지 삭제
     * 
     * developer: 윤수민
     * 
     * @param : url
     * 
     * @return : message, imgPath
     */
    @DeleteMapping("/imageDelete")
    public ResponseEntity<Map<String, Object>> imageUpload(@RequestParam(value = "url") String url) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("bamboo/imageUpload 호출성공");
        try {
            s3Service.delete(url);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

}