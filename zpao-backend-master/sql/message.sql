-- 聊天室消息
create table if not exists yupao.`message`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `text` varchar(512) not null comment '信息',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '聊天室消息';

SELECT u.* FROM user u LEFT JOIN  friend f ON f.toUserId = u.id WHERE f.createUserId = 1 and f.status = 0;