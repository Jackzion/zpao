package com.yupi.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 请求方 id
     */
    private Long toUserId ;
}
