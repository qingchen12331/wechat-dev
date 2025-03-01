package com.qingchen.service;

import com.qingchen.enums.YesOrNo;
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
}
