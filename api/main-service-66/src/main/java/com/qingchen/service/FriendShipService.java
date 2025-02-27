package com.qingchen.service;

import com.qingchen.controller.FriendShipController;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.utils.PagedGridResult;

/**
 * 好友请求服务类
 */
public interface FriendShipService {
    /**
     * 获得朋友关系
     * @param myId
     * @param friendId
     * @return
     */
    public Friendship getFriendShip(String myId,String friendId);
}
