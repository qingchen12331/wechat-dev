package com.qingchen.service;

import com.qingchen.controller.FriendShipController;
import com.qingchen.enums.YesOrNo;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.bo.NewFriendRequestBO;
import com.qingchen.pojo.vo.ContactsVO;
import com.qingchen.utils.PagedGridResult;

import java.util.List;

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

    /**
     * 查询好友列表
     * @param myId
     * @return
     */
    public List<ContactsVO> queryMyFriends(String myId);

    /**
     * 修改好友备注名
     * @param myId
     * @param friendId
     * @param friendRemark
     */
    public void updateFriendRemark(String myId, String friendId, String friendRemark);

    void updateBlackList(String myId, String friendId, YesOrNo yesOrNo);
}
