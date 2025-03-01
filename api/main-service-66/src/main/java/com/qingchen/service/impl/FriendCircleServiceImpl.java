package com.qingchen.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.mapper.FriendCircleMapper;
import com.qingchen.mapper.FriendCircleMapperCustom;
import com.qingchen.pojo.FriendCircle;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.FriendCircleVO;
import com.qingchen.service.FriendCircleService;
import com.qingchen.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FriendCircleServiceImpl extends BaseInfoProperties implements FriendCircleService {
    @Autowired
    private FriendCircleMapper friendCircleMapper;
    @Autowired
    private FriendCircleMapperCustom friendCircleMapperCustom;
    @Transactional
    @Override
    public void publish(FriendCircleBO friendCircleBO) {
        FriendCircle friendCircle = new FriendCircle();
        BeanUtils.copyProperties(friendCircleBO, friendCircle);
        friendCircleMapper.insert(friendCircle);
    }

    @Override
    public PagedGridResult queryList(String userId, Integer page, Integer pageSize) {
        Map<String,Object>map=new HashMap<>();
        map.put("userId",userId);
        //设置分页参数
        Page<FriendCircleVO> pageInfo=new Page<>(page,pageSize);
         friendCircleMapperCustom.queryFriendCircleList(pageInfo, map);


        return setterPagedGridPlus(pageInfo);
    }
}
