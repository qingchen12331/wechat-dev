package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.bo.CommentBO;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.CommentVO;
import com.qingchen.pojo.vo.FriendCircleVO;
import com.qingchen.service.CommentService;
import com.qingchen.service.FriendCircleService;
import com.qingchen.utils.PagedGridResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.qingchen.base.BaseInfoProperties.HEADER_USER_ID;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

@PostMapping("create")
    public GraceJSONResult create(@RequestBody CommentBO commentBO){
    CommentVO commentVO=commentService.createComment(commentBO);
    return GraceJSONResult.ok(commentVO);

}
    @PostMapping("query")
    public GraceJSONResult query(String friendCircleId){
        List<CommentVO> commentVO=commentService.queryAll(friendCircleId);
        return GraceJSONResult.ok(commentVO);

    }


}
