package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nick_name` VARCHAR(50) DEFAULT '' COMMENT '昵称',
  `age` INT DEFAULT NULL COMMENT '年龄',
  `sex` VARCHAR(10) DEFAULT NULL COMMENT '性别（男/女/其他）',
  `address` VARCHAR(500) DEFAULT NULL COMMENT '所在地区',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `role` INT DEFAULT 2 COMMENT '角色（1-管理员，2-普通用户）',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像链接',
  `bio` VARCHAR(200) DEFAULT NULL COMMENT '个性签名/简介',
  `forbid_comment` TINYINT(1) DEFAULT 0 COMMENT '是否禁止评论（0-否，1-是）',
  `forbid_login` TINYINT(1) DEFAULT 0 COMMENT '是否禁止登录（0-否，1-是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX idx_username (`username`),
  UNIQUE INDEX idx_phone (`phone`),
  INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 已有表追加 bio 字段：
-- ALTER TABLE `user` ADD COLUMN `bio` VARCHAR(200) DEFAULT NULL COMMENT '个性签名/简介' AFTER `avatar`;
-- 已有表删除无用 tags 字段（如有）：
-- ALTER TABLE `user` DROP COLUMN `tags`;
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
    private String avatar;
    /** 个性签名/简介 */
    private String bio;
    private Boolean forbidComment;
    private Boolean forbidLogin;
}
