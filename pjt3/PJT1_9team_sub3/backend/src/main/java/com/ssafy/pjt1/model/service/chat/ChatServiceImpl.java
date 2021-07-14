package com.ssafy.pjt1.model.service.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.pjt1.model.dto.chat.ChatMessage;
import com.ssafy.pjt1.model.dto.user.UserDto;
import com.ssafy.pjt1.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ListOperations<String, String> listOps;

    private ObjectMapper objMapper;

    @Autowired
    private UserService userService;

    private ValueOperations<String, String> valOps;

    // 메시지 다 갖고오기
    @Override
    public List<ChatMessage> getMessage(int startNum, int endNum, String room_id) throws IOException {
        String key = "message:" + room_id;
        listOps = redisTemplate.opsForList();
        objMapper = new ObjectMapper();
        List<String> str = listOps.range(key, startNum, endNum);
        List<ChatMessage> list = new ArrayList<>();
        for (String json : str) {
            log.info(">>>>>>>>>>>getMessage{}", json);
            list.add(objMapper.readValue(json, ChatMessage.class));
        }
        return list;
    }

    // 메시지 저장
    @Override
    public void insertMessage(ChatMessage message) throws IOException {
        String key = "message:" + message.getRoom_id();
        listOps = redisTemplate.opsForList();
        log.info("key:{}", key);
        log.info("messages:{}", message.getMsg());
        log.info("여까진 잘 들어옴");
        objMapper = new ObjectMapper();
        String strMsg = objMapper.writeValueAsString(message);
        listOps.leftPush(key, strMsg);
    }

    @Override
    public List<Object> getRoomList(String user_id) throws JsonMappingException, JsonProcessingException {
        valOps = redisTemplate.opsForValue();
        objMapper = new ObjectMapper();
        String key = "roomInfo:" + user_id + ":*";
        Set<String> keys = redisTemplate.keys(key);
        List<Object> roomList = new LinkedList<>();
        for (String opp_id : keys) {// 내 키를 기준으로 상대방 키를 알수있음
            String roomInfoStr = valOps.get(opp_id);
            Map<String, String> roomInfoObj = objMapper.readValue(roomInfoStr, Map.class);
            String roomId = roomInfoObj.get("roomId");
            String recentMsg = getRecentMessage(roomId);
            roomInfoObj.put("recentMsg", recentMsg);
            roomList.add(roomInfoObj);
        }
        return roomList;
    }

    @Override
    public String makeRoom(String user_id, String opp_id) throws IOException {
        String uid = UUID.randomUUID().toString();
        valOps = redisTemplate.opsForValue();
        objMapper = new ObjectMapper();
        // 방 생성하기
        // 내 방과, 상대방 방을 동시에 생성
        UserDto myDto = userService.userDtoById(user_id);
        UserDto oppDto = userService.userDtoById(opp_id);
        Map<String, Object> myRoomInfo = new HashMap<>();
        myRoomInfo.put("roomId", uid);// 방 번호
        myRoomInfo.put("opp_nickName", oppDto.getUser_nickname());// 상대방 닉네임
        myRoomInfo.put("opp_img", oppDto.getUser_image());// 상대방 이미지
        myRoomInfo.put("opp_id", opp_id);// 상대방 아이디
        myRoomInfo.put("is_used", 1);// 사용중

        Map<String, Object> oppRoomInfo = new HashMap<>();
        oppRoomInfo.put("roomId", uid);// 방 번호
        oppRoomInfo.put("opp_nickName", myDto.getUser_nickname());// 상대방 닉네임
        oppRoomInfo.put("opp_img", myDto.getUser_image());// 상대방 이미지
        oppRoomInfo.put("opp_id", user_id);// 상대방 아이디
        oppRoomInfo.put("is_used", 1);// 사용중
        // 채팅방에 넣기
        String myRoomInfoStr = objMapper.writeValueAsString(myRoomInfo);
        String oppRoomInfoStr = objMapper.writeValueAsString(oppRoomInfo);
        valOps.set("roomInfo:" + user_id + ":" + opp_id, myRoomInfoStr);
        valOps.set("roomInfo:" + opp_id + ":" + user_id, oppRoomInfoStr);
        return uid;
    }

    // 마지막 메세지를 갖고오기.
    @Override
    public String getRecentMessage(String room_id) throws JsonMappingException, JsonProcessingException {
        String key = "message:" + room_id;
        listOps = redisTemplate.opsForList();
        objMapper = new ObjectMapper();
        List<String> str = listOps.range(key, 0, 0);
        String res = "";
        // 길이가 0인 경우
        if (str.size() != 0) {
            ChatMessage chat = objMapper.readValue(str.get(0), ChatMessage.class);
            res = chat.getMsg();
        }
        return res;
    }

}
