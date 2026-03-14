package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


/*
CREATE TABLE `look_author` (
  -- 补充主键：数据库表必须有主键，推荐自增BIGINT类型
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `comment_user` VARCHAR(50) DEFAULT NULL COMMENT '被查看的作者/评论用户',
  `user` VARCHAR(50) DEFAULT NULL COMMENT '查看者用户',
  PRIMARY KEY (`id`),
  -- 可选唯一索引：避免同一用户重复记录查看同一作者（根据业务需求选择）
  UNIQUE INDEX idx_user_comment_user (`user`, `comment_user`),
  -- 普通索引：优化按查看者/被查看作者的查询效率
  INDEX idx_comment_user (`comment_user`),
  INDEX idx_user (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户查看作者记录表';

*/

@Data
@TableName("look_author")
public class LookAuthor extends Model<LookAuthor> {

    private String commentUser;

    private String user;

}
