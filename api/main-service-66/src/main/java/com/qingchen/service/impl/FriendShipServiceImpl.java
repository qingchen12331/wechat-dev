package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.enums.YesOrNo;
import com.qingchen.mapper.FriendshipMapper;
import com.qingchen.mapper.FriendshipMapperCustom;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.vo.ContactsVO;
import com.qingchen.service.FriendShipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Transactional
    @Override
    public Friendship getFriendShip(String myId, String friendId) {
        QueryWrapper queryWrapper=new QueryWrapper<FriendRequest>().eq("my_id",myId)
                .eq("friend_id",friendId);

        return friendshipMapper.selectOne(queryWrapper);
    }
    @Transactional
    @Override
    public List<ContactsVO> queryMyFriends(String myId,boolean needBlack) {
        Map<String,Object>map=new HashMap<>();
        map.put("myId",myId);
        map.put("needBlack",needBlack);
        return friendshipMapperCustom.queryMyFriends(map);
    }
    @Transactional
    @Override
    public void updateFriendRemark(String myId, String friendId, String friendRemark) {
        QueryWrapper<Friendship>updateWrapper=new QueryWrapper<>();
        updateWrapper.eq("my_id",myId).eq("friend_id",friendId);
        Friendship friendship=new Friendship();
        friendship.setFriendRemark(friendRemark);
        friendship.setUpdatedTime(LocalDateTime.now());
        friendshipMapper.update(friendship,updateWrapper);

    }
    @Transactional
    @Override
    public void updateBlackList(String myId, String friendId, YesOrNo yesOrNo) {
    QueryWrapper updateWrapper=new QueryWrapper<Friendship>()
            .eq("my_id",myId)
            .eq("friend_id",friendId);
    Friendship friendship=new Friendship();
    friendship.setIsBlack(yesOrNo.type);
    friendship.setUpdatedTime(LocalDateTime.now());
    friendshipMapper.update(friendship,updateWrapper);
    }
    @Transactional
    @Override
    public void delete(String myId, String friendId) {
        QueryWrapper delelteWrapper=new QueryWrapper<Friendship>()
                .eq("my_id",myId)
                .eq("friend_id",friendId);
        friendshipMapper.delete(delelteWrapper);
        QueryWrapper delelteWrapper2=new QueryWrapper<Friendship>()
                .eq("my_id",friendId)
                .eq("friend_id",myId);
        friendshipMapper.delete(delelteWrapper2);
    }
}
