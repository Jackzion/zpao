package com.ziio.zpao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziio.zpao.model.domain.Message;
import com.ziio.zpao.service.MessageService;
import com.ziio.zpao.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Ziio
* @description 针对表【message(聊天室消息)】的数据库操作Service实现
* @createDate 2024-05-26 13:18:59
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

}




