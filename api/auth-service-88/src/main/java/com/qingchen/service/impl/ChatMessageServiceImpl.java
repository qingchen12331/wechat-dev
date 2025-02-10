package com.qingchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingchen.mapper.ChatMessageMapper;
import com.qingchen.pojo.ChatMessage;
import com.qingchen.service.IChatMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天信息存储表 服务实现类
 * </p>
 *
 * @author qingchen
 * @since 2025-02-10
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageService {

}
