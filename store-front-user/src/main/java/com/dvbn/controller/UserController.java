package com.dvbn.controller;

import com.dvbn.param.UserCheckParam;
import com.dvbn.param.UserLoginParam;
import com.dvbn.pojo.User;
import com.dvbn.service.UserService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 检查账号是否可用
     *
     * @param userCheckParam 接收检查的账号，内部有参数检验注解
     * @param result         获取校验的结果
     * @return 返回封装结果对象R
     */
    @PostMapping("/check")
    public Result check(@RequestBody @Validated UserCheckParam userCheckParam, BindingResult result) {

        // 检查是否符合检验注解的规则， 符合返回false，不符合返回tree
        boolean b = result.hasErrors();
        if (b) {
            return Result.fail("账号为空,不可使用！");
        }
        return userService.check(userCheckParam);
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数异常,注册失败!");
        }
        return userService.register(user);
    }

    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserLoginParam userLoginParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数异常,登录失败");
        }

        return userService.login(userLoginParam);
    }
}
