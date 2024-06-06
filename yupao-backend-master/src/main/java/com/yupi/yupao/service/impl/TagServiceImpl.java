package com.yupi.yupao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.model.domain.Tag;
import com.yupi.yupao.service.TagService;
import com.yupi.yupao.mapper.TagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author Ziio
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-06-02 23:04:16
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Resource
    private TagMapper tagMapper;

    @Override
    public List<Tag> filterTags(List<String> tags) {
        List<Tag> list = new ArrayList<>();
        // 去重
        for(String tag : tags){
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tagName",tag);
            tagMapper.selectOne(queryWrapper);
            if(tag==null){
                Tag newTag = new Tag();
                newTag.setTagName(tag);
                list.add(newTag);
            }
        }
        return list;
    }
}




