package com.qingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.mapper.CommentMapper;
import com.qingchen.pojo.Comment;
import com.qingchen.service.ICommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
