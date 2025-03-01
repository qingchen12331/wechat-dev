package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.FriendCircleVO;
import com.qingchen.service.FriendCircleService;
import com.qingchen.utils.PagedGridResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.qingchen.base.BaseInfoProperties.HEADER_USER_ID;

@RestController
@RequestMapping("friendCircle")
public class FriendCircleController {
    @Autowired
    private FriendCircleService friendCircleService;
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody FriendCircleBO friendCircleBO, HttpServletRequest request){
        String userId=request.getHeader(HEADER_USER_ID);
        friendCircleBO.setUserId(userId);
        friendCircleBO.setPublishTime(LocalDateTime.now());
        friendCircleService.publish(friendCircleBO);

        return GraceJSONResult.ok();
    }
    @PostMapping("queryList")
    public GraceJSONResult queryList(String userId, @RequestParam(defaultValue = "1",name = "page")Integer page,
                                     @RequestParam(defaultValue = "10",name = "pageSize")Integer pageSize){
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.error();
        }
        PagedGridResult gridResult = friendCircleService.queryList(userId, page, pageSize);
        List<FriendCircleVO>list=(List<FriendCircleVO>)gridResult.getRows();
        for (FriendCircleVO f:list){
            String friendCircleId = f.getFriendCircleId();
            List<FriendCircleLiked> likedList=friendCircleService.queryLikedFriends(friendCircleId);
            f.setLikedFriends(likedList);
        }
        return GraceJSONResult.ok(gridResult);
    }
    @PostMapping("like")
    public GraceJSONResult like(String friendCircleId,HttpServletRequest request){
        String userId=request.getHeader(HEADER_USER_ID);
        friendCircleService.like(friendCircleId,userId);
        return GraceJSONResult.ok();
    }
    @PostMapping("unlike")
    public GraceJSONResult unlike(String friendCircleId,HttpServletRequest request){
        String userId=request.getHeader(HEADER_USER_ID);
        friendCircleService.unlike(friendCircleId,userId);
        return GraceJSONResult.ok();
    }
}
