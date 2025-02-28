package com.qingchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingchen.pojo.Friendship;
import com.qingchen.pojo.vo.ContactsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 朋友关系表 Mapper 接口
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
public interface FriendshipMapperCustom  {
    public List<ContactsVO> queryMyFriends(@Param("paramMap")Map<String,Object>map);
}
