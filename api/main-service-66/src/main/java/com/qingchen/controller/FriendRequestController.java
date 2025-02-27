package com.qingchen.controller;

import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.service.FriendRequestService;
import com.qingchen.utils.PagedGridResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.qingchen.base.BaseInfoProperties.HEADER_USER_ID;

@RestController
@RequestMapping("friendRequest")
@Slf4j
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;
    @PostMapping("add")
    public GraceJSONResult add(@RequestBody @Valid NewFriendRequestBO newFriendRequestBO){
        friendRequestService.addNewRequest(newFriendRequestBO);

        return GraceJSONResult.ok();
    }
    @PostMapping("queryNew")
    public GraceJSONResult queryNew(HttpServletRequest request, @RequestParam(defaultValue = "1",name = "page"
    )Integer page, @RequestParam(defaultValue = "10",name = "pageSize")Integer pageSize){
        String userId=request.getHeader(HEADER_USER_ID);
        PagedGridResult result = friendRequestService.queryNewFriendList(userId, page, pageSize);
        return GraceJSONResult.ok(result);
    }

}
