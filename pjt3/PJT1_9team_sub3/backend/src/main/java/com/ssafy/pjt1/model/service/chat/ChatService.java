package com.ssafy.pjt1.model.service.chat;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ssafy.pjt1.model.dto.chat.ChatMessage;

public interface ChatService {

    public List<ChatMessage> getMessage(int startNum, int endNum, String room_id) throws IOException;

    public void insertMessage(ChatMessage message) throws IOException;

    public String getRecentMessage(String room_id) throws JsonMappingException, JsonProcessingException;

    public String makeRoom(String user_id, String opp_id) throws IOException;

    public List<Object> getRoomList(String user_id) throws JsonMappingException, JsonProcessingException;

}
