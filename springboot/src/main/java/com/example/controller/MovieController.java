package com.example.controller;

import com.example.common.Result;
import com.example.entity.Movie;
import com.example.entity.User;
import com.example.mapper.MovieMapper;
import com.example.mapper.UserMapper;
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

    @Resource
    private UserMapper userMapper;

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

    /**
     * 按用户ID查询该用户发布的作品
     */
    @GetMapping("/user/{userid}")
    public Result<?> findMovieByUserid(@PathVariable Long userid) {
        List<Movie> list = movieMapper.selectList(
                Wrappers.<Movie>lambdaQuery().eq(Movie::getPostUserId, userid).orderByDesc(Movie::getId));
        return Result.success(list);
    }

    /**
     * 按用户名查询该用户发布的作品（供"关注的人的作品"页面使用）
     */
    @GetMapping("/user/byUsername/{username}")
    public Result<?> findMovieByUsername(@PathVariable String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user == null) {
            return Result.error("-1", "用户不存在");
        }
        List<Movie> list = movieMapper.selectList(
                Wrappers.<Movie>lambdaQuery().eq(Movie::getPostUserId, user.getId()).orderByDesc(Movie::getId));
        return Result.success(list);
    }
}