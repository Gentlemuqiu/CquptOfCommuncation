package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


/*
CREATE TABLE `look_author` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `comment_user` VARCHAR(50) DEFAULT NULL COMMENT '被关注的作者用户名',
  `user` VARCHAR(50) DEFAULT NULL COMMENT '关注者用户名',
  `create_time` DATETIME DEFAULT NULL COMMENT '关注时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX idx_user_comment_user (`user`, `comment_user`),
  INDEX idx_comment_user (`comment_user`),
  INDEX idx_user (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注记录表';

-- 已有表追加 create_time 字段：
-- ALTER TABLE `look_author` ADD COLUMN `create_time` DATETIME DEFAULT NULL COMMENT '关注时间' AFTER `user`;
*/

@Data
@TableName("look_author")
public class LookAuthor extends Model<LookAuthor> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 被关注的作者用户名
     */
    private String commentUser;

    /**
     * 关注者用户名
     */
    private String user;

    /**
     * 关注时间
     */
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date createTime;

}
