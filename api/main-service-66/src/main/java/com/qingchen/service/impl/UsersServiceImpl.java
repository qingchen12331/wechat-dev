package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.enums.Sex;
import com.qingchen.exceptions.GraceException;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.grace.result.ResponseStatusEnum;
import com.qingchen.mapper.UsersMapper;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.ModifyUserBO;
import com.qingchen.service.IUsersService;
import com.qingchen.utils.DesensitizationUtil;
import com.qingchen.utils.LocalDateUtils;
import com.qingchen.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.qingchen.base.BaseInfoProperties.REDIS_USER_ALREADY_UPDATE_WECHAT_NUM;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RedisOperator redis;


    @Override
    public void modifyUserInfo(ModifyUserBO userBO) {
        String wechatNum=userBO.getWechatNum();
        Users pendingUser=new Users();
        String userID= userBO.getUserId();
        if(StringUtils.isBlank(userID)){
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);

        }
        if(StringUtils.isNotBlank(wechatNum)){
            String isExit=redis.get(REDIS_USER_ALREADY_UPDATE_WECHAT_NUM+":"+userID);
            if(StringUtils.isNotBlank(isExit))
                GraceException.display(ResponseStatusEnum.WECHAT_NUM_ALREADY_MODIFIED_ERROR);
        }
        pendingUser.setId(userID);
        pendingUser.setUpdatedTime(LocalDateTime.now());
        BeanUtils.copyProperties(userBO,pendingUser);
        usersMapper.updateById(pendingUser);
        if(StringUtils.isNotBlank(wechatNum)){
            redis.setByDays(REDIS_USER_ALREADY_UPDATE_WECHAT_NUM+":"+userID,userID,365);
        }


    }
}
