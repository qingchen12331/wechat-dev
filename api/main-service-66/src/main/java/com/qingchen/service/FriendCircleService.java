package com.qingchen.service;

import com.qingchen.enums.YesOrNo;
import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.ContactsVO;
import com.qingchen.utils.PagedGridResult;

import java.util.List;

/**
 * 好友请求服务类
 */

public interface FriendCircleService {
    public void publish(FriendCircleBO friendCircleBO);

    /**
     * 分页查询朋友圈图文列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryList(String userId, Integer page, Integer pageSize);

    /**
     * 点赞朋友圈
     * @param friendCiircleId
     * @param userId
     */
    public void like(String friendCiircleId,String userId);
    /**
     * 取消点赞朋友圈
     * @param friendCiircleId
     * @param userId
     */
    public void unlike(String friendCiircleId,String userId);

    /**
     * 查询朋友圈的点赞列表
     * @param friendCircleId
     * @return
     */
    public List<FriendCircleLiked> queryLikedFriends(String friendCircleId);

    /**
     *判断是否点赞过
     * @param friendCircleId
     * @param userId
     * @return
     */
    boolean doILike(String friendCircleId,String userId);
}
