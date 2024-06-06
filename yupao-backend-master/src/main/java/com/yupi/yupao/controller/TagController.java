package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.Message;
import com.yupi.yupao.model.domain.Tag;
import com.yupi.yupao.service.MessageService;
import com.yupi.yupao.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tag")
@CrossOrigin()
@Slf4j
public class TagController {
    @Resource
    private TagService tagService;

    /** 添加一组 tags
     *
     * @param tags
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addTags(@RequestBody String tags) {
        if (tags == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转 json 为 list
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 去重
        List<Tag> newTags = tagService.filterTags(tagList);
        tagService.saveBatch(newTags);
        return ResultUtils.success(true);
    }

    @GetMapping("/list")
    public BaseResponse<List<Tag>> listTeams() {

        List<Tag> list = tagService.list();
        return ResultUtils.success(list);
    }
}
