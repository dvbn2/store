package com.dvbn.controller;

import com.dvbn.param.CartListParam;
import com.dvbn.param.OrderParam;
import com.dvbn.service.OrderService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/save")
    public Result save(@RequestBody OrderParam orderParam) {
        return orderService.save(orderParam);
    }

    @PostMapping("/list")
    public Result list(@RequestBody @Validated CartListParam cartListParam, BindingResult result) {

        if (result.hasErrors()) {
            return Result.fail("参数异常,查询失败");
        }

        return orderService.list(cartListParam.getUserId());
    }
}
