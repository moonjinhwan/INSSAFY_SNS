package com.ssafy.pjt1.model.service.bamboo;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.bamboo.BambooDto;

public interface BambooService {

	public void createBamboo(BambooDto bambooDto);

	public List<BambooDto> getAllList(Map<String, Object> map);

	public List<BambooDto> searchPost(Map<String, Object> map);

	public int isWriter(Map<String, Object> map);

	public int bambooModify(BambooDto bambooDto);

	public int bambooDelete(int bamboo_id);

	public int getTotalCnt();

	public int getSearchCntT(String keyword);

	public int getSearchCntD(String keyword);

	public List<BambooDto> searchPostT(Map<String, Object> map);

	public List<BambooDto> searchPostD(Map<String, Object> map);

}
