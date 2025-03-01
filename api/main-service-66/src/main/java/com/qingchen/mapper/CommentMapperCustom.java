package com.qingchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingchen.pojo.Comment;
import com.qingchen.pojo.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
public interface CommentMapperCustom extends BaseMapper<Comment> {
    public List<CommentVO> queryFriendCircleComments(@Param("paramMap")Map<String,Object>map);

}
