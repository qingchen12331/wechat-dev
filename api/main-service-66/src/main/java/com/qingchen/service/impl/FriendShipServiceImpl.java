package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.mapper.FriendshipMapper;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.pojo.Friendship;
import com.qingchen.service.FriendShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FriendShipServiceImpl extends BaseInfoProperties implements FriendShipService {
@Autowired
private FriendshipMapper friendshipMapper;

    @Override
    public Friendship getFriendShip(String myId, String friendId) {
        QueryWrapper queryWrapper=new QueryWrapper<FriendRequest>().eq("my_id",myId)
                .eq("friend_id",friendId);

        return friendshipMapper.selectOne(queryWrapper);
    }
}
