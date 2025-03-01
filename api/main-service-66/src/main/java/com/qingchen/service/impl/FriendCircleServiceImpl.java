package com.qingchen.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingchen.base.BaseInfoProperties;
import com.qingchen.mapper.FriendCircleLikedMapper;
import com.qingchen.mapper.FriendCircleMapper;
import com.qingchen.mapper.FriendCircleMapperCustom;
import com.qingchen.pojo.FriendCircle;
import com.qingchen.pojo.FriendCircleLiked;
import com.qingchen.pojo.Users;
import com.qingchen.pojo.bo.FriendCircleBO;
import com.qingchen.pojo.vo.FriendCircleVO;
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
public class FriendCircleServiceImpl extends BaseInfoProperties implements FriendCircleService {
    @Autowired
    private FriendCircleMapper friendCircleMapper;
    @Autowired
    private FriendCircleMapperCustom friendCircleMapperCustom;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private FriendCircleLikedMapper friendCircleLikedMapper;
    @Transactional
    @Override
    public void publish(FriendCircleBO friendCircleBO) {
        FriendCircle friendCircle = new FriendCircle();
        BeanUtils.copyProperties(friendCircleBO, friendCircle);
        friendCircleMapper.insert(friendCircle);
    }

    @Override
    public PagedGridResult queryList(String userId, Integer page, Integer pageSize) {
        Map<String,Object>map=new HashMap<>();
        map.put("userId",userId);
        //设置分页参数
        Page<FriendCircleVO> pageInfo=new Page<>(page,pageSize);
         friendCircleMapperCustom.queryFriendCircleList(pageInfo, map);
        log.info(pageInfo.getRecords().toString());

        return setterPagedGridPlus(pageInfo);
    }
    @Transactional
    @Override
    public void like(String friendCiircleId, String userId) {
        //根据朋友圈的主键ID查询归属人(发布人)
        FriendCircle friendCircle = selectFriendCircle(friendCiircleId);
        //根据用户主键ID查询点赞人
        Users users = usersService.getById(userId);
        FriendCircleLiked friendCircleLiked = new FriendCircleLiked();
        friendCircleLiked.setFriendCircleId(friendCiircleId);
        friendCircleLiked.setBelongUserId(friendCircle.getUserId());
        friendCircleLiked.setLikedUserId(userId);
        friendCircleLiked.setLikedUserName(users.getNickname());
        friendCircleLiked.setCreatedTime(LocalDateTime.now());
        friendCircleLikedMapper.insert(friendCircleLiked);
        //点赞过后,朋友圈的对应点赞数累加1
        redis.increment(REDIS_FRIEND_CIRCLE_LIKED_COUNTS+":"+friendCiircleId,1);
        //标记哪个用户点赞过该朋友圈
        redis.setnx(REDIS_DOES_USER_LIKE_FRIEND_CIRCLE+":"+friendCiircleId+":"+userId,userId);
    }
    @Transactional
    @Override
    public void unlike(String friendCiircleId, String userId) {
        //从数据库中删除点赞关系
        QueryWrapper queryWrapper=new QueryWrapper<FriendCircleLiked>()
                .eq("friend_circle_id",friendCiircleId)
                .eq("liked_user_id",userId);
        friendCircleLikedMapper.delete(queryWrapper);
        //取消点赞后,朋友圈的对应点赞数累减一
        redis.decrement(REDIS_FRIEND_CIRCLE_LIKED_COUNTS+":"+friendCiircleId,1);
        //删除标记的那个用户点赞过的朋友圈
        redis.del(REDIS_DOES_USER_LIKE_FRIEND_CIRCLE+":"+friendCiircleId+":"+userId);

    }

    @Override
    public List<FriendCircleLiked> queryLikedFriends(String friendCircleId) {
        QueryWrapper queryWrapper = new QueryWrapper<FriendCircleLiked>()
                .eq("friend_circle_id",friendCircleId);
        return friendCircleLikedMapper.selectList(queryWrapper);
    }

    private FriendCircle selectFriendCircle(String friendCircleId){
        FriendCircle friendCircle = friendCircleMapper.selectById(friendCircleId);
        if (friendCircle == null){
            return null;
        }
        return friendCircle;
    }
}
