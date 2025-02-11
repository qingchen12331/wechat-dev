package com.qingchen.controller;

import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.ModifyUserBO;
import com.qingchen.pojo.vo.UsersVO;
import com.qingchen.service.IUsersService;
import com.qingchen.service.impl.UsersServiceImpl;
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

}
