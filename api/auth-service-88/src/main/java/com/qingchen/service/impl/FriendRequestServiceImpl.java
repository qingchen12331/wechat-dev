package com.qingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.mapper.FriendRequestMapper;
import com.qingchen.pojo.FriendRequest;
import com.qingchen.service.IFriendRequestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 好友请求记录表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements IFriendRequestService {

}
