package com.yupi.yupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.model.domain.Message;
import com.yupi.yupao.service.MessageService;
import com.yupi.yupao.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Ziio
* @description 针对表【message(聊天室消息)】的数据库操作Service实现
* @createDate 2024-05-26 13:18:59
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




