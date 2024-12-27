package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Message;
import com.example.entity.User;
import com.example.mapper.MessageMapper;
import com.example.mapper.MovieMapper;
import com.example.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MovieMapper movieMapper;

    @PostMapping
    public Result<?> save(@RequestBody Message message) {
        String username = message.getUsername();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user.getForbidComment()) {
            return Result.error("-1", "禁止评论");
        }
        message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageMapper.insert(message));
    }

    @PutMapping
    public Result<?> update(@RequestBody Message message) {
        return Result.success(messageMapper.updateById(message));
    }

    /**
     * 删除指定 ID 的消息及其所有子消息
     */
    @Transactional
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deleteMessageAndChildren(id); // 调用递归删除方法
        return Result.success();
    }

    /**
     * 递归删除指定 ID 的消息及其所有子级
     */
    private void deleteMessageAndChildren(Long id) {
        // 查找所有以当前 id 为 parent_id 的子消息
        List<Message> childMessages = messageMapper.selectList(
                Wrappers.<Message>lambdaQuery().eq(Message::getParentId, id)
        );

        // 递归删除每个子消息
        for (Message child : childMessages) {
            deleteMessageAndChildren(child.getId());
        }

        // 删除当前消息
        messageMapper.deleteById(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return Result.success(messageMapper.selectById(id));
    }

    @GetMapping
    public Result<?> findAll() {
        return Result.success(messageMapper.selectList(null));
    }

    @GetMapping("/user/{username}/{type}")
    public Result<?> userComment(@PathVariable String username, @PathVariable Integer type) {
        List<Message> messageList = messageMapper.selectList(
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getUsername, username)
                        .eq(Message::getType, type)
        );
        for (Message message : messageList) {
            Long foreignId = message.getForeignId();
            message.setMovie(movieMapper.selectById(foreignId));
        }
        return Result.success(messageList);
    }

    @GetMapping("/foreign/{id}/{type}")
    public Result<?> foreign(@PathVariable Integer id, @PathVariable Integer type) {
        return Result.success(findByForeign(id, type));
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String search,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery()
                .like(Message::getContent, search)
                .orderByDesc(Message::getId);
        return Result.success(messageMapper.selectPage(new Page<>(pageNum, pageSize), query));
    }

    private List<Message> findByForeign(Integer id, Integer type) {
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery()
                .eq(Message::getType, type)
                .eq(Message::getForeignId, id)
                .orderByDesc(Message::getId);
        List<Message> list = messageMapper.selectList(queryWrapper);
        for (Message message : list) {
            String username = message.getUsername();
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            // 默认一个头像
            message.setAvatar(user.getAvatar());
            Long parentId = message.getParentId();
            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(message::setParentMessage);
        }
        return list;
    }
}
