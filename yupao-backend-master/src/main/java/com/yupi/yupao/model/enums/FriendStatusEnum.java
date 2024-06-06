package com.yupi.yupao.model.enums;

/**
 * 队伍状态枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum FriendStatusEnum {

    NONFRIENDS(0, "未同意"),
    FRIENDS(1, "已同意");

    private int value;

    private String text;

    public static FriendStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        FriendStatusEnum[] values = FriendStatusEnum.values();
        for (FriendStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    FriendStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
