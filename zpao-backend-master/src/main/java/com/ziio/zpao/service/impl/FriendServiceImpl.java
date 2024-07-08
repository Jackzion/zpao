package com.ziio.zpao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziio.zpao.model.domain.Friend;
import com.ziio.zpao.service.FriendService;
import com.ziio.zpao.mapper.FriendMapper;
import org.springframework.stereotype.Service;

/**
* @author Ziio
* @description 针对表【friend(好友)】的数据库操作Service实现
* @createDate 2024-06-05 17:38:26
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

}




