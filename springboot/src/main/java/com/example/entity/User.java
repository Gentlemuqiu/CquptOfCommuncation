package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（建议加密存储）',
  `nick_name` VARCHAR(50) DEFAULT '' COMMENT '昵称',
  `age` INT DEFAULT NULL COMMENT '年龄',
  `sex` VARCHAR(10) DEFAULT NULL COMMENT '性别（如男/女/未知）',
  `address` VARCHAR(500) DEFAULT NULL COMMENT '地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `role` INT DEFAULT 0 COMMENT '角色（0-普通用户，1-管理员）',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '用户标签（多个用逗号分隔）',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像链接',
  `forbid_comment` TINYINT(1) DEFAULT 0 COMMENT '是否禁止评论（0-否，1-是）',
  `forbid_login` TINYINT(1) DEFAULT 0 COMMENT '是否禁止登录（0-否，1-是）',
  PRIMARY KEY (`id`),
  -- 核心索引：保证用户名唯一，优化登录/查询效率
  UNIQUE INDEX idx_username (`username`),
  -- 可选索引：优化按手机号/角色的查询
  UNIQUE INDEX idx_phone (`phone`),
  INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
*/

@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nickName;
    private Integer age;
    private String sex;
    private String address;
    private String phone;
    private Integer role;
    private String tags;
    private String avatar;
    private Boolean forbidComment;
    private Boolean forbidLogin;
}
