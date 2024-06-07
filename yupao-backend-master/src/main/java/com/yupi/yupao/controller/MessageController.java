package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.Message;
import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserTeam;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.TeamUpdateRequest;
import com.yupi.yupao.model.vo.TeamUserVO;
import com.yupi.yupao.service.MessageService;
import com.yupi.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@CrossOrigin(origins = {"http://localhost:3000"})
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Message message, HttpServletRequest request) {
        if (message == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        messageService.save(message);
        return ResultUtils.success(message.getId());
    }

    @GetMapping("/list")
    public BaseResponse<List<Message>> listTeams() {

        List<Message> list = messageService.list();
        return ResultUtils.success(list);

    }
}
