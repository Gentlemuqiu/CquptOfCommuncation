package com.example.controller;

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
                collectMovieMapper.selectOne(Wrappers.<CollectMovie>lambdaQuery()
                        .eq(CollectMovie::getMovieid, collectMovie.getMovieid())
                        .eq(CollectMovie::getUserid, collectMovie.getUserid()));
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

    /**
     * 按 userid + movieid 取消收藏
     */
    @DeleteMapping("/delete")
    public Result<?> deleteByUserAndMovie(
            @RequestParam Integer userid,
            @RequestParam Integer movieid) {
        int n = collectMovieMapper.delete(Wrappers.<CollectMovie>lambdaQuery()
                .eq(CollectMovie::getUserid, userid)
                .eq(CollectMovie::getMovieid, movieid));
        if (n == 0) {
            return Result.error("-1", "未找到该收藏记录");
        }
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
            @RequestParam(required = false) Integer userid, // 新增 userid 参数
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<CollectMovie> query =
                Wrappers.<CollectMovie>lambdaQuery()
                        .orderByDesc(CollectMovie::getId);
        if (StrUtil.isNotBlank(name)) {
            query.like(CollectMovie::getName, name); // 根据名称模糊查询
        }
        if (userid != null) {
            query.eq(CollectMovie::getUserid, userid); // 根据用户ID查询
        }
        IPage<CollectMovie> page =
                collectMovieMapper.selectPage(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

}

