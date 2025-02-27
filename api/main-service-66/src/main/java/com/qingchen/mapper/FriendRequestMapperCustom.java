package com.qingchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.pojo.vo.NewFriendsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.Map;

/**
 * <p>
 * 好友请求记录表 Mapper 接口
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Mapper
public interface FriendRequestMapperCustom  {
    public Page<NewFriendsVO> queryNewFriendList(@Param("page") Page<NewFriendsVO> page,
                                                @Param("paramMap") Map<String,Object> map );
}
