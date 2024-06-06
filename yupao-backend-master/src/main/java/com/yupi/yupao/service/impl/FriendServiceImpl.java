package com.yupi.yupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.mapper.UserMapper;
import com.yupi.yupao.model.domain.Friend;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.service.FriendService;
import com.yupi.yupao.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Ziio
* @description 针对表【friend(好友)】的数据库操作Service实现
* @createDate 2024-06-05 17:38:26
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

}




