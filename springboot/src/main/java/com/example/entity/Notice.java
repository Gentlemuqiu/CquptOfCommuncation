package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

/*
CREATE TABLE `notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '名称',
  `content` TEXT DEFAULT NULL COMMENT '内容',
  `status` VARCHAR(20) DEFAULT 'publish' COMMENT '状态（如publish-发布、draft-草稿、offline-下线）',
  `time` VARCHAR(30) DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`),
  -- 可选索引：优化按状态/发布时间的查询效率
  INDEX idx_notice_status (`status`),
  INDEX idx_notice_time (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

*/


@Data
@TableName("notice")
public class Notice extends Model<Notice> {
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
      * 内容 
      */
    private String content;

    /**
      * 状态 
      */
    private String status;

    /**
      * 发布时间 
      */
    private String time;

}