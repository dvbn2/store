package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.constants.UserConstants;
import com.dvbn.mapper.UserMapper;
import com.dvbn.param.UserCheckParam;
import com.dvbn.param.UserLoginParam;
import com.dvbn.pojo.User;
import com.dvbn.service.UserService;
import com.dvbn.utils.MD5Util;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result check(UserCheckParam userCheckParam) {
        // 1.数据封装
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userCheckParam.getUserName());
        // 2.数据库处理
        Long total = userMapper.selectCount(queryWrapper);
        // 3.数据结果处理

        if (total == 0) {
            log.info("UserServiceImpl.check==>{}", userCheckParam.getUserName());
            return Result.ok("账号不存在可用使用");
        }

        return Result.fail("账号已存在,不可注册");
    }

    @Override
    public Result register(User user) {
        // 1.检查账号是否存在

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());

        Long total = userMapper.selectCount(queryWrapper);
        if (total > 0) {
            log.info("UserServiceImpl.register==>{}", user.getUserName());
            return Result.fail("账号已存在");
        }

        // 2.密码加密处理,加盐
        // 加盐：在密码后面加一段字符串
        String newPwd = MD5Util.encode(user.getPassword() + UserConstants.USER_SLAT);
        user.setPassword(newPwd);
        // 3.插入数据库
        int rows = userMapper.insert(user);

        if (rows == 0) {
            log.info("UserServiceImpl.register==>{}", "注册失败");
            return Result.fail("注册失败,请稍后在试");
        }
        // 4.返回结果
        return Result.ok("注册成功");
    }

    /**
     * 登录业务
     *
     * @param userLoginParam
     * @return
     */
    @Override
    public Result login(UserLoginParam userLoginParam) {
        // 1.将密码加密加盐
        String newPwd = MD5Util.encode(userLoginParam.getPassword() + UserConstants.USER_SLAT);

        // 2.数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userLoginParam.getUserName())
                .eq("password", newPwd);
        User user = userMapper.selectOne(queryWrapper);
        // 3.返回结果
        if (user == null) {
            log.info("UserServiceImpl.register==>{}", "账号或密码错误");
            return Result.fail("账号或密码错误");
        }
        // 不返回password
        // 有@JsonInclude(JsonInclude.Include.NON_NULL)注解 如果值为null 则返回前端的数据中没有password
        user.setPassword(null);
        return Result.ok("登录成功", user);
    }
}
