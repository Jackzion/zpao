package com.ziio.zpao.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 好友
 * @TableName friend
 */
@TableName(value ="friend")
@Data
public class Friend implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接受方
     */
    private Long toUserId;

    /**
     * 请求方
     */
    private Long createUserId;

    /**
     * 0 待添加,1 已添加
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}