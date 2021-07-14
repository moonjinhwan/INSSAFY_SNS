package com.ssafy.pjt1.model.service.study;

import java.util.List;
import java.util.Map;

public interface StudyService {

	public List<Map<String, Object>> getPromoList(Map<String, Object> map);

	public List<Integer> getStudyId(String login_id);

	public Map<String, Object> getStudyInfo(Integer board_id);

	public List<Map<String, Object>> getAllList(Map<String, Object> map);

	public int getTotalCnt();

	public void request(Map<String, Object> map);

	public List<Map<String, Object>> getRequestList(int board_id);

	public void requestProcess(Map<String, Object> map);

	public void secession(Map<String, Object> map);

	public void reSubscription(Map<String, Object> map);

	public List<Map<String, Object>> myRequestList(String user_id);

	public List<Map<String, Object>> myStudyRequest(Map<String, Object> map);

}
