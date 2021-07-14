package com.ssafy.pjt1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.pjt1.model.dto.post.PostDto;
import com.ssafy.pjt1.model.service.BoardService;
import com.ssafy.pjt1.model.service.post.PostService;
import com.ssafy.pjt1.model.service.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    public static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String PERMISSION = "No Permission";

    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private RedisService redisService;

    /*
     * 기능: 보드 내 포스트 작성
     * 
     * developer: 윤수민
     * 
     * @param : user_id, board_id, post_title, post_description, post_image,
     * post_iframe, post_header, post_state
     * 
     * @return : message
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> postCreate(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/create 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            String user_id = (String) param.get("user_id");
            int board_id = (int) param.get("board_id");
            map.put("user_id", user_id);
            map.put("board_id", board_id);
            if (boardService.isUnSubscribed(map) != 0) {
                PostDto postDto = new PostDto();
                postDto.setUser_id(user_id);
                postDto.setBoard_id(board_id);
                postDto.setPost_title((String) param.get("post_title"));
                postDto.setPost_description((String) param.get("post_description"));
                postDto.setPost_image((String) param.get("post_image"));
                postDto.setPost_iframe((String) param.get("post_iframe"));
                postDto.setPost_header((String) param.get("post_header"));
                postDto.setPost_state((int) param.get("post_state"));
                postService.createPost(postDto);
                resultMap.put("message", SUCCESS);
            } else {
                resultMap.put("message", PERMISSION);
            }
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 디테일
     * 
     * developer: 윤수민
     * 
     * @param : login_id, post_id
     * 
     * @return : message, PostDto, isScrapped, isLiked, like_count, commentList,
     * writer_nickname, writer_image
     */
    @GetMapping("/getPostById")
    public ResponseEntity<Map<String, Object>> getPostById(@RequestParam(value = "post_id") int post_id,
            @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/getPostById 호출성공");
        try {
            PostDto postDto = postService.getPostById(post_id);
            if (postDto != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("user_id", login_id);
                map.put("post_id", post_id);
                int isScrapped = postService.isUnScrapped(map);
                int isLiked = postService.isUnLiked(map);
                List<Map<String, Object>> commentList = postService.getComment(post_id);
                String writer_nickname = postService.getWriterName(postDto.getUser_id());
                String writer_image = postService.getWriterImage(postDto.getUser_id());

                resultMap.put("postDto", postDto);
                resultMap.put("isScrapped", isScrapped);
                resultMap.put("isLiked", isLiked);
                resultMap.put("commentList", commentList);
                resultMap.put("writer_nickname", writer_nickname);
                resultMap.put("writer_image", writer_image);
                resultMap.put("message", SUCCESS);
            } else {
                // id에 맞는 게시글 존재하지 않으면 NULL 리턴
                resultMap.put("message", "NULL");
            }

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 수정
     * 
     * developer: 윤수민
     * 
     * @param : PostDto, login_id
     * 
     * @return : message
     */
    @PutMapping("/modify")
    public ResponseEntity<Map<String, Object>> postModify(@RequestBody PostDto postDto,
            @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/modify 호출 성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("post_id", postDto.getPost_id());
            map.put("login_id", login_id);
            if (postService.isWriter(map) != 0) {
                if (postService.postModify(postDto) == 1) {
                    resultMap.put("message", SUCCESS);
                }
            } else {
                resultMap.put("message", PERMISSION);
            }

        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("error", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 중고장터 거래 진행 변경
     * 
     * developer: 윤수민
     * 
     * @param : post_id, post_state, login_id
     * 
     * @return : message
     */
    @PutMapping("/modifyState")
    public ResponseEntity<Map<String, Object>> stateModify(@RequestParam(value = "post_id") int post_id,
            @RequestParam(value = "post_state") int post_state, @RequestParam(value = "login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/modifyState 호출 성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("post_id", post_id);
            map.put("login_id", login_id);
            if (postService.isWriter(map) != 0) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("post_id", post_id);
                map2.put("post_state", post_state);
                if (postService.stateModify(map2) == 1) {
                    resultMap.put("message", SUCCESS);
                }
            } else {
                resultMap.put("message", PERMISSION);
            }

        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("error", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 삭제
     * 
     * developer: 윤수민
     * 
     * @param : post_id, login_id
     * 
     * @return : message
     */
    @DeleteMapping("/delete/{post_id}/{login_id}")
    public ResponseEntity<Map<String, Object>> postDelete(@PathVariable("post_id") int post_id,
            @PathVariable("login_id") String login_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/delete 호출성공");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("post_id", post_id);
            map.put("login_id", login_id);
            if (postService.isWriter(map) != 0) {

                if (postService.postDelete(post_id) >= 1) {
                    // postService.deleteScrapAll(post_id);
                    // postService.deleteLikeAll(post_id);
                    // postService.deleteCommentAll(post_id);
                    resultMap.put("message", SUCCESS);
                }
            } else {
                resultMap.put("message", PERMISSION);
            }

        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("error", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 스크랩
     * 
     * developer: 윤수민
     * 
     * @param : post_id, user_id
     * 
     * @return : message
     */
    @PostMapping("/scrap")
    public ResponseEntity<Map<String, Object>> scrap(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/scrap 호출성공");
        try {
            String user_id = (String) param.get("user_id");
            int post_id = (int) param.get("post_id");
            int board_id = boardService.getIdbyPostId(post_id);
            Map<String, Object> map = new HashMap<>();

            map.put("user_id", user_id);
            map.put("board_id", board_id);
            if (boardService.isUnSubscribed(map) != 0) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("user_id", user_id);
                map2.put("post_id", post_id);

                int count = postService.isScrapped(map2);
                if (count == 0) {
                    logger.info("스크랩 추가");
                    postService.scrap(map2);
                } else {
                    int count2 = postService.isUnScrapped(map2);
                    if (count2 == 0) {
                        // 전에 스크랩한 이력이 있지만 현재는 아닌 경우
                        postService.updateScrap(map2);
                    } else {
                        logger.info("스크랩 삭제");
                        postService.deleteScrap(map2);
                    }
                }
                resultMap.put("message", SUCCESS);
            } else {
                resultMap.put("message", "No Subscription");
            }

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 좋아요
     * 
     * developer: 윤수민
     * 
     * @param : post_id, user_id
     * 
     * @return : message
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likePost(@RequestBody Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/like 호출성공");
        try {
            String user_id = (String) param.get("user_id");
            int post_id = (int) param.get("post_id");
            int board_id = boardService.getIdbyPostId(post_id);
            Map<String, Object> map = new HashMap<>();

            map.put("user_id", user_id);
            map.put("board_id", board_id);

            if (boardService.isUnSubscribed(map) != 0) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("user_id", user_id);
                map2.put("post_id", post_id);

                int count = postService.isLiked(map2);
                if (count == 0) {
                    logger.info("좋아요 클릭");
                    postService.like(map2);
                    postService.plusCount(post_id, board_id);
                } else {
                    int count2 = postService.isUnLiked(map2);
                    if (count2 == 0) {
                        // 전에 좋아요한 이력이 있지만 현재는 아닌 경우
                        postService.updateLike(map2);
                        postService.plusCount(post_id, board_id);
                    } else {
                        logger.info("좋아요 삭제");
                        postService.unlike(map2);
                        postService.minusCount(post_id, board_id);

                    }
                }
                resultMap.put("message", SUCCESS);
            } else {
                resultMap.put("message", "No Subscription");
            }

        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 포스트 리스트
     * 
     * developer: 윤수민
     * 
     * @param : board_id, user_id, page, size
     * 
     * @return : message, isLastPage
     * postList(post_id,user_id,post_date,post_title,post_description,
     * post_image,post_iframe,post_header,post_state,like_count,
     * comment_count,user_nickname, user_image, isLiked(1:좋아요누른 상태 0:좋아요 취소상태 리턴값 없는 경우:좋아요
     * 안누른 상태), isScrapped(isLiked와 마찬가지))
     */
    @GetMapping("/getPostList")
    public ResponseEntity<Map<String, Object>> getPostByList(@RequestParam(value = "board_id") int board_id,
            @RequestParam(value = "user_id") String user_id, @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("post/getPostList 호출성공");
        try {
            int totalCnt = postService.getTotalPostCnt(board_id);
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");

            Map<String, Object> map = new HashMap<>();
            map.put("board_id", board_id);
            map.put("user_id", user_id);
            map.put("start", page * size);
            map.put("size", size);
            List<Map<String, Object>> postList = postService.getPostList(map);
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 중고장터 포스트 리스트 (판매완료 제외)
     * 
     * developer: 윤수민
     * 
     * @param : board_id, login_id
     * 
     * @return : message, isLastPage
     * postList(post_id,user_id,post_date,post_title,post_description,
     * post_image,post_iframe,post_header,post_state,like_count, comment_count, user_nickname, user_image)
     */
    @GetMapping("/getSalesList")
    public ResponseEntity<Map<String, Object>> getSalesList(@RequestParam(value = "board_id") int board_id,
            @RequestParam(value = "login_id") String login_id, @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("board/getSalesList 호출성공");
        try {
            int totalCnt = postService.getSalesCnt();
            if (totalCnt > (page + 1) * size) {
                resultMap.put("isLastPage", "false");
            } else if (totalCnt > page * size) {
                resultMap.put("isLastPage", "true");
            } else {
                resultMap.put("isLastPage", "No data");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("board_id", board_id);
            map.put("login_id", login_id);
            map.put("start", page * size);
            map.put("size", size);
            List<Map<String, Object>> postList = postService.getSalesList(map);
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            logger.error("실패", e);
            resultMap.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 전체 보드 포스트 검색 (최신순, 인기순, 타입별)
     * 
     * developer: 윤수민
     * 
     * @param : sort, keyword, page, size, type(header, title, description)
     * 
     * @return : postList, message
     */
    @GetMapping("/searchPost")
    public ResponseEntity<Map<String, Object>> searchPost(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size, @RequestParam(value = "type") String type) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/post/searchBoard 호출 성공");
        try {
            int totalCnt;
            // totalCnt = postService.getAllSearchPostCnt(keyword);
            List<Map<String, Object>> postList;
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", keyword);
            map.put("start", page * size);
            map.put("size", size);
            if (sort.equals("new")) {
                if(type.equals("header")){
                    logger.info("최신순 포스트 헤더 검색");
                    totalCnt = postService.getAllSearchPostCntH(keyword);
                    postList = postService.searchPostNewH(map);
                }else if(type.equals("title")){
                    logger.info("최신순 포스트 타이틀 검색");
                    totalCnt = postService.getAllSearchPostCntT(keyword);
                    postList = postService.searchPostNewT(map);
                }else{
                    logger.info("최신순 포스트 내용 검색");
                    totalCnt = postService.getAllSearchPostCntD(keyword);
                    postList = postService.searchPostNewD(map);
                }
                // postList = postService.searchPostNew(map);
            } else {
                if(type.equals("header")){
                    logger.info("좋아요순 포스트 헤더 검색");
                    totalCnt = postService.getAllSearchPostCntH(keyword);
                    postList = postService.searchPostPopularH(map);
                }else if(type.equals("title")){
                    logger.info("좋아요순 포스트 타이틀 검색");
                    totalCnt = postService.getAllSearchPostCntT(keyword);
                    postList = postService.searchPostPopularT(map);
                }else{
                    logger.info("좋아요순 포스트 내용 검색");
                    totalCnt = postService.getAllSearchPostCntD(keyword);
                    postList = postService.searchPostPopularD(map);
                }
                // postList = postService.searchPostPopular(map);
            }
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("검색 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 보드 내 포스트 검색 (최신순, 인기순)
     * 
     * developer: 윤수민
     * 
     * @param : sort, keyword, board_id, page,size,user_id,type
     * 
     * @return : postList, message, isLastPage
     */
    @GetMapping("/board/searchPost")
    public ResponseEntity<Map<String, Object>> searchBoardPost(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "board_id") String board_id,
            @RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
            @RequestParam(value = "user_id") String user_id, @RequestParam(value = "type") String type) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/post/board/searchPost 호출 성공");
        try {
            List<Map<String, Object>> postList;
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", keyword);
            map.put("board_id", board_id);
            map.put("start", page * size);
            map.put("size", size);
            map.put("user_id", user_id);

            int totalCnt;
            totalCnt = postService.getSearchPostCnt(map);
            
            if (sort.equals("new")) {
                if(type.equals("header")){
                    logger.info("최신순 포스트 헤더 검색");
                    totalCnt = postService.getSearchPostCntH(map);
                    postList = postService.boardPostNewH(map);
                }else if(type.equals("title")){
                    logger.info("최신순 포스트 타이틀 검색");
                    totalCnt = postService.getSearchPostCntT(map);
                    postList = postService.boardPostNewT(map);
                }else{
                    logger.info("최신순 포스트 내용 검색");
                    totalCnt = postService.getSearchPostCntD(map);
                    postList = postService.boardPostNewD(map);
                }
                // postList = postService.boardPostNew(map);
            } else {
                if(type.equals("header")){
                    logger.info("좋아요순 포스트 헤더 검색");
                    totalCnt = postService.getSearchPostCntH(map);
                    postList = postService.boardPostPopularH(map);
                }else if(type.equals("title")){
                    logger.info("좋아요순 포스트 타이틀 검색");
                    totalCnt = postService.getSearchPostCntT(map);
                    postList = postService.boardPostPopularT(map);
                }else{
                    logger.info("좋아요순 포스트 내용 검색");
                    totalCnt = postService.getSearchPostCntD(map);
                    postList = postService.boardPostPopularD(map);
                }
                // postList = postService.boardPostPopular(map);
            }
            if (totalCnt > (page + 1) * size)
                resultMap.put("isLastPage", "false");
            else if (totalCnt > page * size)
                resultMap.put("isLastPage", "true");
            else
                resultMap.put("isLastPage", "No data");
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("검색 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    /*
     * 기능: 중고장터 내 판매중인 포스트만 검색 (최신순, 인기순)
     * 
     * developer: 윤수민
     * 
     * @param : sort, keyword, board_id, page, size, user_id
     * 
     * @return : postList, message
     */
    @GetMapping("/board/searchMarketPost")
    public ResponseEntity<Map<String, Object>> searchMarketPost(@RequestParam(value = "sort") String sort,
            @RequestParam(value = "keyword") String keyword, @RequestParam(value = "board_id") String board_id,
            @RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
            @RequestParam(value = "user_id") String user_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        logger.info("/post/board/searchMarketPost 호출 성공");
        try {
            List<Map<String, Object>> postList;
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", keyword);
            map.put("board_id", board_id);
            map.put("start", page * size);
            map.put("size", size);
            map.put("user_id", user_id);
            int totalCnt = postService.searchSalesCnt(map);
            if (totalCnt > (page + 1) * size) {
                resultMap.put("isLastPage", "false");
            } else if (totalCnt > page * size) {
                resultMap.put("isLastPage", "true");
            } else {
                resultMap.put("isLastPage", "No data");
            }
            if (sort.equals("new")) {
                logger.info("최신순 포스트 검색");
                postList = postService.marketPostNew(map);
            } else {
                logger.info("좋아요순 포스트 검색");
                postList = postService.marketPostPopular(map);
            }
            resultMap.put("postList", postList);
            resultMap.put("message", SUCCESS);
        } catch (Exception e) {
            resultMap.put("message", FAIL);
            logger.error("검색 호출 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

}
