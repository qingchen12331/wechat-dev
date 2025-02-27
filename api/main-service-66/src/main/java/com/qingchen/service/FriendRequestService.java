package com.qingchen.service;

import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.utils.PagedGridResult;

/**
 * 好友请求服务类
 */
public interface FriendRequestService {
    public void addNewRequest(NewFriendRequestBO newFriendRequestBO);

    /**
     * 查询新朋友列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryNewFriendList(String userId,Integer page,Integer pageSize);

    /**
     * 通过好友请求
     * @param friendRequestId
     * @param friendRemark
     */
   public void passNewFriend(String friendRequestId,String friendRemark);
}
