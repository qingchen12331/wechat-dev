package com.qingchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingchen.exceptions.MyCustomException;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.RegistLoginBO;
import com.qingchen.pojo.vo.UsersVO;
import com.qingchen.service.IUsersService;
import com.qingchen.tasks.SMSTask;
import com.qingchen.utils.IPUtil;
import com.qingchen.utils.MyInfo;
import com.qingchen.utils.RedisOperator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.qingchen.base.BaseInfoProperties.*;

@RestController
@RequestMapping("/passport")
@Slf4j
public class PassportController {
    @Resource
    private SMSTask smsTask;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ValidationAutoConfiguration validationAutoConfiguration;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(String mobile,HttpServletRequest request) throws Exception {
        if(StringUtils.isBlank(mobile)){
            return GraceJSONResult.error();
        }
        //获得用户的手机号/ip
        String userIp=IPUtil.getRequestIp(request);
        //限制该用户的手机号/ip在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE+":"+userIp,mobile);
        String code =(int)((Math.random()*9+1)*100000)+"";

        smsTask.sendSMSInTask(MyInfo.getMobile(),code);
        //把验证码存入redis中,用于后续的注册和登录
        redis.set(MOBILE_SMSCODE+":"+mobile,code,30*60);

        return GraceJSONResult.ok();

    }
    @PostMapping("regist")
    public GraceJSONResult regist(@RequestBody @Valid RegistLoginBO registLogonBo,HttpServletRequest request)throws Exception{
        String mobile=registLogonBo.getMobile();
        String code=registLogonBo.getSmsCode();
        String nickname=registLogonBo.getNickname();
        //从Redis中获得验证码进行校验判断是否匹配
        String redisCode=redis.get(MOBILE_SMSCODE+":"+mobile);
        if(StringUtils.isBlank(redisCode)||!redisCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        //根据moblie查询数据库,如果用户存在,则提示不能重复注册
//        LambdaQueryWrapper<Users> queryWrapper=new LambdaQueryWrapper();
//        if(queryWrapper.eq(Users::getMobile,mobile)!=null){
//            return GraceJSONResult.errorMsg("不能重复注册");
//        }
        Users users=usersService.queryMobileIfExist(mobile);
        if(users==null){
            //如果查询数据库中用户为空,则表示用户没有注册过,则需要进行用户信息数据的入库
            Users user=usersService.createUsers(mobile,nickname);
//        Users user=new Users();
//        user.setMobile(mobile);
//        user.setNickname(registLogonBo.getNickname());
//        log.info("新创建的对象为:{}",user);
//        usersService.save(user);
            //用户注册成功后,删除redis中的短信验证码使其失效
            redis.del(MOBILE_SMSCODE+":"+mobile);
            //设置用户分布式会话,保存用户的token令牌,存储到redis中
            String uToken=TOKEN_USER_PREFIX+SYMBOL_DOT+ UUID.randomUUID();
            redis.set(REDIS_USER_TOKEN+":"+user.getId(),uToken);//设置分布式会话
            UsersVO usersVO=new UsersVO();
            BeanUtils.copyProperties(user,usersVO);
            usersVO.setUserToken(uToken);


            //返回用户给前端

            return GraceJSONResult.ok(usersVO);
        }else{
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_ALREADY_EXIST_ERROR);
        }

    }
    @PostMapping("login")
    public GraceJSONResult login(@RequestBody @Valid RegistLoginBO registLoginBO, HttpServletRequest request) throws Exception{
        String mobile=registLoginBO.getMobile();
        String code=registLoginBO.getSmsCode();
        String redisCode=redis.get(MOBILE_SMSCODE+":"+mobile);
        //检查验证码
        if(StringUtils.isBlank(code)||!redisCode.equalsIgnoreCase(redisCode)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);

        }
        //查询数据库中是否有该用户
        Users user=usersService.queryMobileIfExist(mobile);

        if(user==null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        redis.del(MOBILE_SMSCODE+":"+mobile);
        String uToken=TOKEN_USER_PREFIX+SYMBOL_DOT+ UUID.randomUUID();
        redis.set(REDIS_USER_TOKEN+":"+user.getId(),uToken);//设置分布式会话
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserToken(uToken);
        return GraceJSONResult.ok(usersVO);

    }
    @PostMapping("logout")
    public GraceJSONResult logouut(@RequestParam String userId,HttpServletRequest request) throws Exception{
        redis.del(REDIS_USER_TOKEN+":"+userId);
        return GraceJSONResult.ok();
    }



}
