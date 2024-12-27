package com.example.controller;

import com.example.common.Result;
import com.example.entity.Movie;
import com.example.mapper.MovieMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieMapper movieMapper;

    @PostMapping
    public Result<?> save(@RequestBody Movie movie) {
        movieMapper.insert(movie);
        return Result.success();
    }


    @PutMapping
    public Result<?> update(@RequestBody Movie movie) {
        movieMapper.updateById(movie);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        movieMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(movieMapper.selectById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        List<Movie> list = movieMapper.selectList(null);
        return Result.success(list);
    }


    @GetMapping("/search")
    public Result<?> search(@RequestParam String name) {
        List<Movie> list = movieMapper.selectList(
                Wrappers.<Movie>lambdaQuery().like(Movie::getName, name));
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<?> findPage(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize) {
        LambdaQueryWrapper<Movie> query =
                Wrappers.<Movie>lambdaQuery().orderByDesc(Movie::getId);
        if (StrUtil.isNotBlank(search)) {
            query.like(Movie::getName, search).or();
        }
        IPage<Movie> page =
                movieMapper.selectPage(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/page/area")
    public Result<?> findPageArea(
            @RequestParam(required = false, defaultValue = "") String area,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize) {
        LambdaQueryWrapper<Movie> query =
                Wrappers.<Movie>lambdaQuery().orderByDesc(Movie::getId);
        query.eq(Movie::getArea, area);
        IPage<Movie> page =
                movieMapper.selectPage(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }


}