package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt1.model.service.UserService;
import com.ssafy.pjt1.model.service.redis.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/addFunc")
public class AddFunctionController {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "유저랭킹", notes = "보드마다 유저 랭킹 보여주기(좋아요를 많이 받은 순서)")
    @GetMapping("/getUserRank")
    public ResponseEntity<Map<String, Object>> getUserRank(@RequestParam("board_id") int board_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String key = "sortSet:func:userRank:" + String.valueOf(board_id);
            String top3UserInfo = (String) valOps.get(key);
            List<Map<String, String>> user = mapper.readValue(top3UserInfo, List.class);
            resultMap.put("message", SUCCESS);
            resultMap.put("topUsers", user);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
