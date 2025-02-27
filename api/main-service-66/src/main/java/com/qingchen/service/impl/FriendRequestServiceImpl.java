package com.qingchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.enums.FriendRequestVerifyStatus;
import com.qingchen.enums.YesOrNo;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.mapper.FriendRequestMapper;
import com.qingchen.mapper.FriendRequestMapperCustom;
import com.qingchen.mapper.FriendshipMapper;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.pojo.vo.NewFriendsVO;
import com.qingchen.service.FriendRequestService;
import com.qingchen.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.cmc.PendInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FriendRequestServiceImpl extends BaseInfoProperties implements FriendRequestService {
    @Autowired
    private FriendRequestMapper friendRequestMapper;
    @Autowired
    private FriendRequestMapperCustom friendRequestMapperCustom;
    @Autowired
    private FriendshipMapper friendshipMapper;
    @Transactional
    @Override
    public void addNewRequest(NewFriendRequestBO FriendRequestBO) {
        //先删除以前的记录
        QueryWrapper queryWrapper = new QueryWrapper<FriendRequest>()
                .eq("my_id",FriendRequestBO.getMyId())
                .eq("friend_id",FriendRequestBO.getFriendId());
        friendRequestMapper.delete(queryWrapper);

        //再添加新的记录
        FriendRequest pendingFriendRequest = new FriendRequest();
        BeanUtils.copyProperties(FriendRequestBO,pendingFriendRequest);
        pendingFriendRequest.setVerifyStatus(FriendRequestVerifyStatus.WAIT.type);
        pendingFriendRequest.setRequestTime(LocalDateTime.now());
        friendRequestMapper.insert(pendingFriendRequest);
    }

    @Override
    public PagedGridResult queryNewFriendList(String userId, Integer page, Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("mySelfId",userId);
        Page<NewFriendsVO> pageInfo=new Page<>(page,pageSize);
        friendRequestMapperCustom.queryNewFriendList(pageInfo,map);

        return setterPagedGridPlus(pageInfo);
    }
    @Transactional
    @Override
    public void passNewFriend(String friendRequestId, String friendRemark) {
        FriendRequest friendRequest=getSingle(friendRequestId);
        String mySelfId=friendRequest.getFriendId();//通过方的用户id
        String myFriendId=friendRequest.getMyId();
        LocalDateTime nowTime=LocalDateTime.now();
        Friendship friendShipSelf=new Friendship();
        friendShipSelf.setMyId(mySelfId);
        friendShipSelf.setFriendId(myFriendId);
        friendShipSelf.setFriendRemark(friendRemark);
        friendShipSelf.setIsBlack(YesOrNo.NO.type);
        friendShipSelf.setIsMsgIgnore(YesOrNo.NO.type);
        friendShipSelf.setCreatedTime(nowTime);
        friendShipSelf.setUpdatedTime(nowTime);
        Friendship friendshipOpposite =new Friendship();
        friendshipOpposite.setMyId(myFriendId);
        friendshipOpposite.setFriendId(mySelfId);
        friendshipOpposite.setFriendRemark(friendRequest.getFriendRemark());
        friendshipOpposite.setIsBlack(YesOrNo.NO.type);
        friendshipOpposite.setIsMsgIgnore(YesOrNo.NO.type);
        friendshipOpposite.setCreatedTime(nowTime);
        friendshipOpposite.setUpdatedTime(nowTime);
        friendshipMapper.insert(friendShipSelf);
        friendshipMapper.insert(friendshipOpposite);
        friendRequest.setVerifyStatus(FriendRequestVerifyStatus.SUCCESS.type);
        friendRequestMapper.updateById(friendRequest);
        QueryWrapper updateWrapper=new QueryWrapper<FriendRequest>().eq("my_id",myFriendId)
                .eq("friend_id",mySelfId);
        FriendRequest requestOpposite=new FriendRequest();
        requestOpposite.setVerifyStatus(FriendRequestVerifyStatus.SUCCESS.type);
        friendRequestMapper.update(requestOpposite,updateWrapper);
    }
    private FriendRequest getSingle(String friendRequestId){
        return friendRequestMapper.selectById(friendRequestId);
    }


}
