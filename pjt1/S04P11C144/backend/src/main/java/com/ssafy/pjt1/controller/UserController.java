package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssafy.pjt1.model.dto.UserDto;
import com.ssafy.pjt1.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            if (userService.loginDao(userDto)) {
                resultMap.put("res", "로그인 성공");
            } else {
                resultMap.put("res", "로그인 실패");
            }
        } catch (Exception e) {
            System.out.println("로그인 실패");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/confirm/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody UserDto userDto, HttpServletResponse response,
            HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            userService.joinService(userDto);// 조인 서비스 호출
            resultMap.put("res", "1");
        } catch (Exception e) {
            System.out.println("회원가입 실패");
            resultMap.put("res", "0");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}