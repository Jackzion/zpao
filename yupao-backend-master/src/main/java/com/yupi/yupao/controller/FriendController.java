package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.mapper.FriendMapper;
import com.yupi.yupao.model.domain.Friend;
import com.yupi.yupao.model.domain.Message;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserTeam;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.enums.FriendStatusEnum;
import com.yupi.yupao.model.vo.TeamUserVO;
import com.yupi.yupao.service.FriendService;
import com.yupi.yupao.service.MessageService;
import com.yupi.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friend")
@CrossOrigin(origins = {"http://localhost:3000"})
@Slf4j
public class FriendController {

    @Resource
    private FriendService friendService;

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private UserService userService;

    // 添加好友
    @PostMapping("/add")
    public BaseResponse<Boolean> addFriend(@RequestBody Long toUserId, HttpServletRequest request) {
        if (toUserId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 验证要添加的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User toUser = userService.getById(toUserId);
        if(toUser==null){
            return new BaseResponse<>(ErrorCode.NULL_ERROR);
        }
        // 不能重复添加好友
        QueryWrapper<Friend> queryFriendWrapper = new QueryWrapper<>();
        queryFriendWrapper.eq("toUserId", toUserId);
        queryFriendWrapper.eq("teamId",loginUser.getId() );
        long hasUserJoinTeam = friendService.count(queryFriendWrapper);
        if (hasUserJoinTeam > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已添加改好友,不可重复添加");
        }
        // 添加
        Friend friend = new Friend();
        friend.setCreateUserId(loginUser.getId());
        friend.setToUserId(toUserId);
        boolean res =  friendService.save(friend);
        return ResultUtils.success(res);
    }

    // 删除好友
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFriend(@RequestBody Long toUserId, HttpServletRequest request) {
        if (toUserId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 验证要删除的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User toUser = userService.getById(toUserId);
        if(toUser==null){
            return new BaseResponse<>(ErrorCode.NULL_ERROR);
        }
        // 删除
        QueryWrapper<Friend> queryFriendWrapper = new QueryWrapper<>();
        queryFriendWrapper.eq("toUserId", toUserId);
        queryFriendWrapper.eq("teamId",loginUser.getId() );
        boolean res =  friendService.remove(queryFriendWrapper);
        return ResultUtils.success(res);
    }

    // 自己的好友列表
    @GetMapping("/list/myFriends")
    public BaseResponse<List<User>> listMyFriends(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<User> friends =  friendMapper.selectUserWithFriends(loginUser.getId(), FriendStatusEnum.FRIENDS.getValue());

        return ResultUtils.success(friends);
    }

    // 未同意 的好友列表
    @GetMapping("/list/nonFriends")
    public BaseResponse<List<User>> listNonFriends(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<User> friends =  friendMapper.selectUserWithFriends(loginUser.getId(),FriendStatusEnum.NONFRIENDS.getValue());
        return ResultUtils.success(friends);
    }
}
