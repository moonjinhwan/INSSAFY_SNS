package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.checkList.CheckListItemDto;
import com.ssafy.pjt1.model.service.checkList.CheckListItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/CheckList")
public class CheckListItemController {
    public static final Logger logger = LoggerFactory.getLogger(CalendarItemController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Autowired
    private CheckListItemService service;

    /*
     * developer: 문진환
     * 
     * @param : checkListDto
     * 
     * @return : message
     */
    // create
    @ApiOperation(value = "체크리스트 생성", notes = "체크리스트 생성")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCheckLsit(
            @RequestBody @ApiParam(value = "checkList item의 정보를 DTO에 주입") CheckListItemDto item) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/create 호출");
        logger.info("check item : {}", item);
        try {
            service.createCheckLsit(item);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error("error", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    // read
    @ApiOperation(value = "체크리스트 불러오기", notes = "체크리스트 불러오기")
    @GetMapping("/read/{board_id}")
    public ResponseEntity<Map<String, Object>> selectCheckLsit(
            @PathVariable @ApiParam(value = "board_id 주입") String board_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/read 호출");
        logger.info("board_id : {}", board_id);
        try {
            List<CheckListItemDto> list = service.selectCheckLsit(board_id);
            resultMap.put("message", SUCCESS);
            resultMap.put("checkList", list);
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error("error", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    // u
    @ApiOperation(value = "체크리스트 업데이트", notes = "체크리스트 업데이트")
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCheckLsit(
            @RequestBody @ApiParam(value = "체크리스트 폼값") CheckListItemDto item) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/update 호출");
        logger.info("item : {}", item);
        try {
            int res = service.updateCheckLsit(item);
            if (res == 1) {
                resultMap.put("message", SUCCESS);
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.NO_CONTENT;
            }

        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error("error", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    // d
    @ApiOperation(value = "체크리스트 삭제", notes = "체크리스트 삭제")
    @PutMapping("delete")
    public ResponseEntity<Map<String, Object>> deleteCheckLsit(
            @RequestParam @ApiParam(value = "check_list_item_id 주입") String check_list_item_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/delete 호출");
        logger.info("check_list_item_id : {}", check_list_item_id);
        try {
            int res = service.deleteCheckList(check_list_item_id);
            if (res == 1) {
                resultMap.put("message", SUCCESS);
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.NO_CONTENT;
            }
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error("error", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}