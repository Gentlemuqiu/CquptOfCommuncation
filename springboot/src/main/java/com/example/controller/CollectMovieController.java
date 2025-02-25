package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.CollectMovie;
import com.example.mapper.CollectMovieMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/collectMovie")
public class CollectMovieController {

    @Resource
    private CollectMovieMapper collectMovieMapper;

    @PostMapping
    public Result<?> save(@RequestBody CollectMovie collectMovie) {
        CollectMovie collectMovie1 =
                collectMovieMapper.selectOne(new QueryWrapper<CollectMovie>()
                        .eq("movieid", collectMovie.getMovieid())
                        .eq("userid", collectMovie.getUserid()));
        if (collectMovie1 != null) {
            return Result.error("-1", "您已收藏过了");
        }
        collectMovieMapper.insert(collectMovie);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody CollectMovie collectMovie) {
        collectMovieMapper.updateById(collectMovie);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        collectMovieMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(collectMovieMapper.selectById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<CollectMovie> list = collectMovieMapper.selectList(null);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer userId, // 新增 userId 参数
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<CollectMovie> query =
                Wrappers.<CollectMovie>lambdaQuery()
                        .orderByDesc(CollectMovie::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(CollectMovie::getName, name); // 根据名称模糊查询
        }
        if (userId != null) {
            query.eq(CollectMovie::getUserid, userId); // 根据用户ID查询
        }
        IPage<CollectMovie> page =
                collectMovieMapper.selectPage(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

}

