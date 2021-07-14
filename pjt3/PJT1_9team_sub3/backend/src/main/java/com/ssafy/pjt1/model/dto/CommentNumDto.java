package com.ssafy.pjt1.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("comNum") // 테이블명으로 이해
public class CommentNumDto {
    @Id
    String id;
    String num;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public CommentNumDto(String id, String num) {
        this.id = id;
        this.num = num;
    }
}