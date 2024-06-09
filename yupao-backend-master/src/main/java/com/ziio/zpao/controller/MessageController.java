package com.ziio.zpao.controller;

import com.ziio.zpao.common.BaseResponse;
import com.ziio.zpao.common.ErrorCode;
import com.ziio.zpao.common.ResultUtils;
import com.ziio.zpao.exception.BusinessException;
import com.ziio.zpao.model.domain.Message;
import com.ziio.zpao.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
