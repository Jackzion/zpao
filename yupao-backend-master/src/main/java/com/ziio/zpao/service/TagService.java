package com.ziio.zpao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ziio.zpao.model.domain.Tag;

import java.util.List;
import java.util.Map;

/**
* @author Ziio
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-06-02 23:04:16
*/
public interface TagService extends IService<Tag> {
    /**
     * tags 筛选
     * @param tags
     * @return
     */
    Map<Boolean, List<Tag>> filterTags(List<Tag> tags);

    /**
     * add tags
     * @param tags
     * @return
     */
    boolean saveTags(List<Tag> tags);
}
