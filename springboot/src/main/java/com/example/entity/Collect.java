package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/*
CREATE TABLE `collect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '电影名称',
  `content` TEXT DEFAULT NULL COMMENT '评论内容',
  `user_id` INT DEFAULT NULL COMMENT '用户id',
  `time` DATETIME DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';
*/

@Data
@TableName("collect")
public class Collect extends Model<Collect> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 电影名称
      */
    private String name;

    /**
      * 评论内容
      */
    private String content;

    /**
      * 用户id
      */
    private Integer userId;

    /**
      * 收藏时间
      */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

}
