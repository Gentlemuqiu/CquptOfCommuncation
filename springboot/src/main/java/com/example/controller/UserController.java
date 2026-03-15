package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        // 只通过用户名+密码查找，不限制 role（角色由数据库决定，前端无需传入）
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword()));
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        if (res.getForbidLogin()) {
            return Result.error("-1", "因账号违规已被禁用");
        }
        // 密码脱敏：不返回密码字段
        res.setPassword(null);
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        user.setNickName(user.getUsername());
        user.setRole(2);
        userMapper.insert(user);
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    // 用户验证接口：通过用户名和手机号验证用户
    @PostMapping("/resetPassVerify")
    public Result<?> verifyUser(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("-1", "用户名和手机号不能为空");
        }
        User existingUser = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPhone, user.getPhone()));
        if (existingUser == null) {
            return Result.error("-1", "用户名或手机号不匹配，请确认信息是否正确");
        }
        return Result.success();
    }

    // 重置密码接口
    @PostMapping("/resetPass")
    public Result<?> resetPassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String phone = params.get("phone");
        String password = params.get("password");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(phone)) {
            return Result.error("-1", "请输入用户名和手机号");
        }

        if (StrUtil.isBlank(password)) {
            return Result.error("-1", "新密码不能为空");
        }

        User existingUser = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPhone, phone));

        if (existingUser == null) {
            return Result.error("-1", "用户不存在");
        }

        // 更新密码
        existingUser.setPassword(password);
        userMapper.updateById(existingUser);

        return Result.success("密码重置成功！");
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        // 不允许通过此接口修改密码
        user.setPassword(null);
        userMapper.updateById(user);
        return Result.success();
    }

    /**
     * 修改密码（需校验旧密码）
     */
    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody Map<String, Object> params) {
        Object idObj = params.get("id");
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");

        if (idObj == null || StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return Result.error("-1", "参数不完整");
        }
        if (newPassword.length() < 6) {
            return Result.error("-1", "新密码长度不能少于 6 位");
        }

        Integer userid = Integer.valueOf(idObj.toString());
        User user = userMapper.selectById(userid);
        if (user == null) {
            return Result.error("-1", "用户不存在");
        }
        if (!user.getPassword().equals(oldPassword)) {
            return Result.error("-1", "当前密码不正确");
        }

        user.setPassword(newPassword);
        userMapper.updateById(user);
        return Result.success("密码修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userMapper.selectById(id));
    }

    @GetMapping("/username/{username}")
    public Result<?> getByUsername(@PathVariable String username) {
        return Result.success(userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username)));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(userMapper.selectList(null));
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getNickName, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }
}
