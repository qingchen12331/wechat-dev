package com.qingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.mapper.FriendshipMapper;
import com.qingchen.pojo.Friendship;
import com.qingchen.service.IFriendshipService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 朋友关系表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship> implements IFriendshipService {

}
