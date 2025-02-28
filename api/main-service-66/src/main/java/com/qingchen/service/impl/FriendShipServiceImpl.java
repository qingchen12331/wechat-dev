package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.mapper.FriendshipMapper;
import com.qingchen.mapper.FriendshipMapperCustom;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.vo.ContactsVO;
import com.qingchen.service.FriendShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FriendShipServiceImpl extends BaseInfoProperties implements FriendShipService {
@Autowired
private FriendshipMapper friendshipMapper;
@Autowired
private FriendshipMapperCustom friendshipMapperCustom;

    @Override
    public Friendship getFriendShip(String myId, String friendId) {
        QueryWrapper queryWrapper=new QueryWrapper<FriendRequest>().eq("my_id",myId)
                .eq("friend_id",friendId);

        return friendshipMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ContactsVO> queryMyFriends(String myId) {
        Map<String,Object>map=new HashMap<>();
        map.put("myId",myId);
        return friendshipMapperCustom.queryMyFriends(map);
    }
}
