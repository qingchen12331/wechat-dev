package com.qingchen.service;

import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.bo.CommentBO;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.CommentVO;
import com.qingchen.utils.PagedGridResult;

import java.util.List;

/**
 * 好友请求服务类
 */

public interface CommentService {
    /**
     * 创建发表评论
     * @param commentBO
     */

    public CommentVO createComment(CommentBO commentBO);
    public List<CommentVO>queryAll(String friendCircleId);

}
