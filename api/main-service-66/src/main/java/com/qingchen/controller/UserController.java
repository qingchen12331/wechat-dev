package com.qingchen.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.ModifyUserBO;
import com.qingchen.pojo.vo.UsersVO;
import com.qingchen.service.IUsersService;
import com.qingchen.service.impl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("userinfo")
public class UserController extends BaseInfoProperties {
    @Autowired
    private IUsersService usersService;
    @PostMapping("modify")
    public GraceJSONResult modify(@RequestBody ModifyUserBO userBO){
        //修改用户信息
        usersService.modifyUserInfo(userBO);
        //返回最新用户信息
        String userId=userBO.getUserId();



        return GraceJSONResult.ok(getUserInfo(userId,true));
    }
    private UsersVO getUserInfo(String userId,boolean needToken){
        Users lastestUser =usersService.getById(userId);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(lastestUser,usersVO);
        if(needToken){
            String uToken=TOKEN_USER_PREFIX+SYMBOL_DOT+ UUID.randomUUID();
            redis.set(REDIS_USER_TOKEN+":"+userId,uToken);
            usersVO.setUserToken(uToken);

        }
        return usersVO;
    }
    @PostMapping("get")
    public GraceJSONResult get(@RequestParam("userId")String userId){
        return GraceJSONResult.ok(getUserInfo(userId,false));
    }
    @PostMapping("updateFace")
    public GraceJSONResult updateFace(@RequestParam("userId") String userId,@RequestParam("face") String face){
        ModifyUserBO userBO=new ModifyUserBO();
        userBO.setUserId(userId);
        userBO.setFace(face);
        usersService.modifyUserInfo(userBO);
        return GraceJSONResult.ok(getUserInfo(userId,true));
    }
    @PostMapping("updateFriendCircleBg")
    public GraceJSONResult updateFriendCircleBg(@RequestParam("userId") String userId,@RequestParam("friendCircleBg") String friendCircleBg){
        ModifyUserBO userBO=new ModifyUserBO();
        userBO.setUserId(userId);
        userBO.setFriendCircleBg(friendCircleBg);
        usersService.modifyUserInfo(userBO);
        return GraceJSONResult.ok(getUserInfo(userId,true));
    }
    @PostMapping("updateChatBg")
    public GraceJSONResult updateChatBg(@RequestParam("userId") String userId,@RequestParam("chatBg") String chatBg){
        ModifyUserBO userBO=new ModifyUserBO();
        userBO.setUserId(userId);
        userBO.setChatBg(chatBg);
        usersService.modifyUserInfo(userBO);
        return GraceJSONResult.ok(getUserInfo(userId,true));
    }
    @PostMapping("queryFriend")
    public GraceJSONResult queryFriend(String queryString ,HttpServletRequest request){
    if(StringUtils.isBlank(queryString))
    {
        return GraceJSONResult.error();
    }
        Users friend = usersService.getByWechatNumOrMobile(queryString);
    if(friend==null)
    {
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FRIEND_NOT_EXIST_ERROR);
    }
    String myId=request.getHeader(HEADER_USER_ID);
    if(friend.getId().equals(myId)){
        return GraceJSONResult.errorCustom(ResponseStatusEnum.CAN_NOT_ADD_SELF_FRIEND_ERROR);
    }
    return GraceJSONResult.ok(friend);

    }


}
