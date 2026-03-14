package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/*
CREATE TABLE `message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '标题',
  `content` TEXT DEFAULT NULL COMMENT '内容',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '评论人',
  `time` VARCHAR(30) DEFAULT NULL COMMENT '评论时间',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父ID',
  `foreign_id` BIGINT DEFAULT NULL COMMENT '关联id',
  `type` INT DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`),
  -- 可选索引：优化按父ID/关联ID/类型的查询效率
  INDEX idx_parent_id (`parent_id`),
  INDEX idx_foreign_id (`foreign_id`),
  INDEX idx_type (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息/评论表';
*/

@Data
@TableName("message")
public class Message extends Model<Message> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 内容
      */
    private String title;
    private String content;

    /**
      * 评论人
      */
    private String username;

    /**
      * 评论时间
      */
    private String time;

    /**
      * 父ID
      */
    private Long parentId;

    @TableField(exist = false)
    private Message parentMessage;

    /**
     * 关联id
     */
    private Long foreignId;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private Movie movie;
    private Integer type;


}