package com.yupi.yupao.mapper;

import com.yupi.yupao.model.domain.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yupao.model.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Ziio
* @description 针对表【friend(好友)】的数据库操作Mapper
* @createDate 2024-06-05 17:38:26
* @Entity com.yupi.yupao.model.domain.Friend
*/
public interface FriendMapper extends BaseMapper<Friend> {

    // 获得好友列表的详细信息
    List<User> selectUserWithFriends(Long userId,int isFriend);
}




