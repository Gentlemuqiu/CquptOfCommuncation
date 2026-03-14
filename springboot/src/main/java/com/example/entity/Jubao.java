package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

/*
CREATE TABLE `jubao` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comment_user` VARCHAR(50) DEFAULT NULL COMMENT '被举报人',
  `user` VARCHAR(50) DEFAULT NULL COMMENT '举报人',
  `comment_id` INT DEFAULT NULL COMMENT '评论id',
  `content` TEXT DEFAULT NULL COMMENT '举报内容',
  PRIMARY KEY (`id`),
  -- 可选索引：优化按评论ID/举报人查询举报记录的效率
  INDEX idx_comment_id (`comment_id`),
  INDEX idx_user (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';
*/

@Data
@TableName("jubao")
public class Jubao extends Model<Jubao> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 被举报人
      */
    private String commentUser;

    /**
      * 举报人
      */
    private String user;


    /**
      * 评论id
      */
    private Integer commentId;

    /**
      * 举报内容
      */
    private String content;

}
