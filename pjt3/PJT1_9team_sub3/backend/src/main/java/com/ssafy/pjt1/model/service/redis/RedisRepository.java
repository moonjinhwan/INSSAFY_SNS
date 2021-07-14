package com.ssafy.pjt1.model.service.redis;

import com.ssafy.pjt1.model.dto.CommentNumDto;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<CommentNumDto, String> {
}