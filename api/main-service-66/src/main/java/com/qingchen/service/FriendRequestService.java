package com.qingchen.service;

import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.utils.PagedGridResult;

/**
 * 好友请求服务类
 */
public interface FriendRequestService {
    public void addNewRequest(NewFriendRequestBO newFriendRequestBO);
    public PagedGridResult queryNewFriendList(String userId,Integer page,Integer pageSize);
}
