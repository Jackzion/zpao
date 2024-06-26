package com.ziio.zpao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ziio.zpao.common.BaseResponse;
import com.ziio.zpao.common.ErrorCode;
import com.ziio.zpao.common.ResultUtils;
import com.ziio.zpao.exception.BusinessException;
import com.ziio.zpao.mapper.FriendMapper;
import com.ziio.zpao.model.domain.Friend;
import com.ziio.zpao.model.domain.User;
import com.ziio.zpao.model.request.FriendRequest;
import com.ziio.zpao.service.FriendService;
import com.ziio.zpao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public BaseResponse<Boolean> addFriend(@RequestBody FriendRequest friendRequest, HttpServletRequest request) {
        if (friendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long toUserId = friendRequest.getToUserId();
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
        queryFriendWrapper.eq("createUserId",loginUser.getId() );
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

    // 接受好友
    @PostMapping("/admit")
    public BaseResponse<Boolean> admitFriend(@RequestBody FriendRequest friendRequest, HttpServletRequest request) {
        if (friendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long toUserId = friendRequest.getToUserId();
        User loginUser = userService.getLoginUser(request);
        // 验证要同意的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User toUser = userService.getById(toUserId);
        if(toUser==null){
            return new BaseResponse<>(ErrorCode.NULL_ERROR);
        }
        // 修改 status 为同意 （1）
        UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("createUserId", toUserId);
        updateWrapper.eq("toUserId",loginUser.getId() );
        updateWrapper.set("status",1);
        boolean res =  friendService.update(updateWrapper);
        return ResultUtils.success(res);
    }

    // 删除(拒绝)好友
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFriend(@RequestBody FriendRequest friendRequest, HttpServletRequest request) {
        // 效验请求
        if (friendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long toUserId = friendRequest.getToUserId();
        User loginUser = userService.getLoginUser(request);
        // 验证要删除的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User toUser = userService.getById(toUserId);
        if(toUser==null){
            return new BaseResponse<>(ErrorCode.NULL_ERROR);
        }
        // 删除
        QueryWrapper<Friend> queryFriendWrapper = new QueryWrapper<>();
        queryFriendWrapper.eq("toUserId", loginUser.getId());
        queryFriendWrapper.eq("createUserId",toUserId );
        boolean res =  friendService.remove(queryFriendWrapper);
        return ResultUtils.success(res);
    }

    // 自己的好友列表
    @GetMapping("/list/myFriends")
    public BaseResponse<List<User>> listMyFriends(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<User> friends =  friendMapper.selectUserWithFriends(loginUser.getId());

        return ResultUtils.success(friends);
    }

    // 未同意 的好友列表
    @GetMapping("/list/nonFriends")
    public BaseResponse<List<User>> listNonFriends(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<User> friends =  friendMapper.selectUserWithNonFriends(loginUser.getId());
        return ResultUtils.success(friends);
    }
}
