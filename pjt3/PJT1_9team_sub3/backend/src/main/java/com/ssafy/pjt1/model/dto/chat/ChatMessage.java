package com.ssafy.pjt1.model.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String room_id;
    private String user_id;
    private String opp_id;
    private String msg;
    private String date;
    private String opp_nickName;
}
