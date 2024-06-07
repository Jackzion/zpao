package com.yupi.yupao.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.model.domain.Tag;
import com.yupi.yupao.service.TagService;
import com.yupi.yupao.mapper.TagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<Boolean, List<Tag>> filterTags(List<Tag> tags) {
//        List<Tag> dupList = new ArrayList<>();
//        List<Tag> nonDupList = new ArrayList<>();
        // 筛选
//        for(String tag : tags){
//            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("tagName",tag);
//            tagMapper.selectOne(queryWrapper);
//            if(tag==null){
//                Tag newTag = new Tag();
//                newTag.setTagName(tag);
//                nonDupList.add(newTag);
//            }else{
//                Tag newTag = new Tag();
//                newTag.setTagName(tag);
//                nonDupList.add(newTag);
//            }
//        }
        // 使用partitioningBy将列表分成两个列表，true -> 重复 ，false -> 不重复
        Map<Boolean, List<Tag>> partitionedMap = tags.stream()
                .collect(Collectors.partitioningBy(tag -> {
                    // 判断是否在表中存在，筛选
                    QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("tagName",tag.getTagName());
                    Tag tag1 = tagMapper.selectOne(queryWrapper);
                    if (tag1!=null) {
                        return true;
                    }
                    return false;
                }));

        return partitionedMap;
    }

    @Override
    public boolean saveTags(List<Tag> tagList) {
        // 将重复 tag ，进行筛选。
        Map<Boolean, List<Tag>> booleanListMap = filterTags(tagList);
        List<Tag> unDupTags = booleanListMap.get(false);
        List<Tag> dupTags = booleanListMap.get(true);
        // 不重复，all insert
        if(!unDupTags.isEmpty()){
            for(Tag tag : unDupTags){
                tagMapper.insert(tag);
            }
        }
        // 重复，update counts ++
        if(!dupTags.isEmpty()){
            List<String> TagsName = dupTags.stream().map(Tag::getTagName).collect(Collectors.toList());
            UpdateWrapper<Tag> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tagName",TagsName);
            updateWrapper.setSql("counts = counts + 1");
        }
        return true;
    }

}




