package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.service.JwtService;
import com.ssafy.pjt1.model.service.MailSendService;
import com.ssafy.pjt1.model.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/account")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MailSendService mss;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     * 기능: 로그인
     * 
     * developer: 문진환
     * 
     * @param UserDto
     * 
     * @return ResultMap
     * 
     * time: 2021/01/23
     */
    @PostMapping("/confirm/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/confirm/login 잘 들어옴");
        try {
            UserDto loginUser = userService.login(userDto);
            logger.info("로그인 객체 : " + loginUser.getUser_email());
            if (loginUser != null
                    && passwordEncoder.matches(userDto.getUser_password(), loginUser.getUser_password())) {// inpt값과 암호화
                                                                                                           // 패스워드 비교->
                                                                                                           // true/false
                                                                                                           // 반환
                String token = jwtService.create("userid", loginUser.getUser_email(), "auth-token");// key, data,
                                                                                                    // subject
                logger.info("로그인 토큰정보 : {}", token);
                // 토큰 정보는 response의 헤더로 보내고 나머지는 Map에 담는다.
                resultMap.put("auth-token", token);
                resultMap.put("userDto", userDto);
                resultMap.put("message", "SUCCESS");
                status = HttpStatus.ACCEPTED;
            } else {
                logger.info("로그인 FAIL");
                resultMap.put("message", "FAIL");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 회원가입
     * 
     * developer: 윤수민
     * 
     * @param UserDto
     * 
     * @return ResultMap * time:
     */
    @PostMapping("/confirm/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/confirm/join 호출 성공");
        try {
            String id = userService.getId();
            logger.info(id);
            userDto.setUser_id(id);

            // 패스워드 암호화해서 저장
            String encoded_password = passwordEncoder.encode(userDto.getUser_password());
            userDto.setUser_password(encoded_password);
            userService.join(userDto);// 조인 서비스 호출
            // 임의의 authKey 생성 & 이메일 발송
            String authKey = mss.sendAuthMail(userDto.getUser_email());

            userDto.setUser_authKey(authKey);

            Map<String, String> map = new HashMap<String, String>();
            map.put("user_email", userDto.getUser_email());
            map.put("user_authKey", userDto.getUser_authKey());
            System.out.println(map);

            // DB에 authKey 업데이트
            userService.updateAuthKey(map);
            resultMap.put("message", "SUCCESS");
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", "FAIL");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 이메일 두번 중복체크
     * 
     * developer: 윤수민
     * 
     * @param
     * 
     * @return time:
     */
    @PostMapping("/confirm/emailCheck")
    public ResponseEntity<Map<String, Object>> emailCheck(@RequestBody String user_email) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("comfirm/emailcheck 호출 성공");
        try {
            UserDto existUser = userService.emailCheck(user_email);
            if (existUser != null) {

                resultMap.put("message", "SUCCESS");
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", "SUCCESS");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", "FAIL");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 메일에서 링크를 누른 경우
     * 
     * developer: 윤수민
     * 
     * @param:
     * 
     * @return:
     */
    @GetMapping("/joinConfirm")
    public ResponseEntity<Map<String, Object>> joinConfirm(@RequestParam Map<String, String> map) {
        userService.updateAuthStatus(map);
        logger.info("이메일 버튼 눌림");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 패스워드 찾기
     * 
     * developer: 윤수민
     * 
     * @param :
     * 
     * @return :
     */
    @PostMapping("/confirm/sendPwMail")
    public ResponseEntity<Map<String, Object>> sendPwMail(@RequestBody UserDto userDto) {
        String email = userDto.getUser_email();
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/confirm/sendPwEmail 호출 성공");
        try {
            String pw = mss.sendPwMail(email);
            Map<String, String> map = new HashMap<>();
            // 패스워드 암호화해서 저장
            String encoded_password = passwordEncoder.encode(pw);
            map.put("pw", encoded_password);
            map.put("email", email);
            userService.updatePw(map);
            resultMap.put("message", "SUCCESS");
        } catch (Exception e) {
            resultMap.put("message", "FAIL");
            logger.error(" 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 마이페이지
     * 
     * developer: 문진환
     * 
     * @param : user_email
     * 
     * @return ResultMap
     */
    @GetMapping("/user/{user_email}")
    public ResponseEntity<Map<String, Object>> userInfo(@PathVariable("user_email") String user_email,
            HttpServletResponse response, HttpSession session, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/user/user_email 호출성공");
        logger.info("저장된 토큰: " + request.getHeader("auth-token"));
        if (jwtService.isUsable(request.getHeader("auth-token"))) {
            try {
                UserDto userDto = userService.userInfo(user_email);
                if (userDto != null) {
                    resultMap.put("userDto", userDto);
                    resultMap.put("message", "SUCCESS");
                }
            } catch (Exception e) {
                resultMap.put("message", "FAIL");
            }
        } else {
            logger.info("토큰 인증 실패");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 내 정보 수정(이미지, 닉네임, 지역, 기수)
     * 
     * developer: 문진환
     * 
     * @param :
     * 
     * @return
     */
    @PostMapping("/user/modify")
    public ResponseEntity<Map<String, Object>> user_modify(@RequestBody UserDto userDto, HttpServletResponse response,
            HttpSession session, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/modify 호출 성공");
        try {
            if (userService.user_modify(userDto) == 1) {
                resultMap.put("message", "SUCCESS");
            } else {
                resultMap.put("message", "FAIL");
            }
        } catch (Exception e) {
            resultMap.put("message", "FAIL");
            logger.error("수정 실패", e);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
    /*
     * 기능: 패스워드 변경
     * 
     * developer: 문진환
     * 
     * @param :
     * 
     * @return
     */

    /*
     * 기능: 회원 탈퇴
     * 
     * developer: 문진환
     * 
     * @param :
     * 
     * @return
     */

}