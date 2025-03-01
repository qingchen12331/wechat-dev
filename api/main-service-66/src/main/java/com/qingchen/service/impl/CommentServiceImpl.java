package com.qingchen.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.grace.result.GraceJSONResult;
import com.qingchen.mapper.*;
import com.qingchen.pojo.Comment;
import com.qingchen.pojo.FriendCircle;
import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.CommentBO;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.CommentVO;
import com.qingchen.pojo.vo.FriendCircleVO;
import com.qingchen.service.CommentService;
import com.qingchen.service.FriendCircleService;
import com.qingchen.service.IUsersService;
import com.qingchen.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private CommentMapperCustom commentMapperCustom;
    @Override
    public CommentVO createComment(CommentBO commentBO) {
        //新增留言
        Comment pendingComment = new Comment();
        BeanUtils.copyProperties(commentBO,pendingComment);
        pendingComment.setCreatedTime(LocalDateTime.now());
        //插入完后会返回最新的id
        commentMapper.insert(pendingComment);
        //留言后的最新评论数据需要返回给前端(提供前端做的扩展数据)
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(pendingComment,commentVO);
        Users commentUser=usersService.getById(commentBO.getCommentUserId());
        commentVO.setCommentUserNickname(commentUser.getNickname());
        commentVO.setCommentUserFace(commentUser.getFace());
        commentVO.setCommentId(pendingComment.getId());

        return commentVO;
    }

    @Override
    public List<CommentVO> queryAll(String friendCircleId) {
        Map<String,Object> map = new HashMap();
        map.put("friendCircleId",friendCircleId);

        return commentMapperCustom.queryFriendCircleComments(map);
    }
    @Transactional
    @Override
    public void deleteComment(String commentUserId, String commentId, String friendCircleId) {
        QueryWrapper queryWrapper = new QueryWrapper<Comment>()
                .eq("id",commentId)
                .eq("friend_circle_id",friendCircleId)
                .eq("comment_user_id",commentUserId);
        commentMapper.delete(queryWrapper);
    }
}
