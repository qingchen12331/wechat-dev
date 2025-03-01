package com.qingchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.pojo.FriendCircle;
import com.qingchen.pojo.vo.FriendCircleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 朋友圈表 Mapper 接口
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
public interface FriendCircleMapperCustom  {

    Page<FriendCircleVO> queryFriendCircleList(@Param("page") Page<FriendCircleVO> page, @Param("paramMap")Map<String,Object>map);
}
