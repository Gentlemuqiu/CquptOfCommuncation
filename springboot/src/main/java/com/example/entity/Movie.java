package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/*
CREATE TABLE `movie` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_user_id` BIGINT DEFAULT NULL COMMENT '发布用户ID',
  `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '信息标题',
  `date` DATE DEFAULT NULL COMMENT '发布日期',
  `img` VARCHAR(500) DEFAULT '' COMMENT '封面图片链接',
  `url` VARCHAR(500) DEFAULT NULL COMMENT '视频链接',
  `description` TEXT DEFAULT NULL COMMENT '信息简介',
  `area` VARCHAR(100) DEFAULT '' COMMENT '信息分类（如校园招聘、实习信息等）',
  `zan` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `cai` INT NOT NULL DEFAULT 0 COMMENT '踩数',
  PRIMARY KEY (`id`),
  INDEX idx_post_user_id (`post_user_id`),
  INDEX idx_movie_area (`area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就业信息表';

-- 已有表追加 url 字段：
-- ALTER TABLE `movie` ADD COLUMN `url` VARCHAR(500) DEFAULT NULL COMMENT '视频链接' AFTER `img`;
*/

@Data
@TableName("movie")
public class Movie extends Model<Movie> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long postUserId;

    /**
      * 名称
      */
    private String name;

    /**
      * 上映日期
      */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    /**
      * 封面图片
      */
    private String img;

    /**
      * 视频链接（可为空）
      */
    private String url;

    private String description;
    private String area;

    private Integer zan;
    private Integer cai;

}
