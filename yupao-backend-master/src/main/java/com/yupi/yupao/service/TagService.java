package com.yupi.yupao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.domain.Tag;

import java.util.List;

/**
* @author Ziio
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-06-02 23:04:16
*/
public interface TagService extends IService<Tag> {
    /**
     * tags 去重
     * @param tags
     * @return
     */
    List<Tag> filterTags(List<String> tags);
}
