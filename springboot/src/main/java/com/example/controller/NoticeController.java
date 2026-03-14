package com.example.controller;

import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import java.util.*;
import java.math.BigDecimal;

/*
INSERT INTO `user` (
  `username`, 
  `password`, 
  `nick_name`, 
  `age`, 
  `sex`, 
  `address`, 
  `phone`, 
  `role`, 
  `tags`, 
  `avatar`, 
  `forbid_comment`, 
  `forbid_login`
) VALUES (
  'admin',  -- 用户名（唯一，管理员默认账号）
  '123456',
  '系统管理员',  -- 昵称
  30,  -- 年龄
  '男',  -- 性别
  '北京市海淀区中关村',  -- 地址
  '13800138000',  -- 手机号（唯一）
  1,  -- 角色：1=管理员（核心要求）
  '管理员,系统,后台',  -- 标签
  'https://example.com/avatar/admin.png',  -- 头像链接
  0,  -- 允许评论（0=否）
  0   -- 允许登录（0=否）
);
*/

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeMapper noticeMapper;

    @PostMapping
    public Result<?> save(@RequestBody Notice notice) {
        notice.setTime(DateUtil.today());
        noticeMapper.insert(notice);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Notice notice) {
        noticeMapper.updateById(notice);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        noticeMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(noticeMapper.selectById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Notice> list = noticeMapper.selectList(null);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String name,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Notice> query = Wrappers.<Notice>lambdaQuery().orderByDesc(Notice::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(Notice::getName, name);
        }
        IPage<Notice> page = noticeMapper.selectPage(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }


}