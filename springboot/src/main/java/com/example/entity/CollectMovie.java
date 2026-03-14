package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


/*
CREATE TABLE `collect_movie` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '名称',
  `img` VARCHAR(500) DEFAULT NULL COMMENT '图片',
  `link` VARCHAR(500) DEFAULT NULL COMMENT '链接',
  `userid` INT DEFAULT NULL COMMENT '用户ID',
  `movieid` INT DEFAULT NULL COMMENT '电影ID',
  PRIMARY KEY (`id`),
  -- 可选：添加联合索引，优化按用户+电影查询收藏的速度（根据业务需求选择）
  UNIQUE INDEX idx_user_movie (`userid`, `movieid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影收藏表';

*/

@Data
@TableName("collect_movie")
public class CollectMovie extends Model<CollectMovie> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 名称 
      */
    private String name;

    /**
      * 图片 
      */
    private String img;

    /**
      * 链接 
      */
    private String link;
    private Integer userid;
    private Integer movieid;

}