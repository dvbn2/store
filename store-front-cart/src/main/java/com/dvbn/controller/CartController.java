package com.dvbn.controller;

import com.dvbn.param.CartListParam;
import com.dvbn.param.CartSaveParam;
import com.dvbn.pojo.Cart;
import com.dvbn.service.CartService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping("/save")
    public Result save(@RequestBody @Validated CartSaveParam cartSaveParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数为空，添加失败");
        }

        return cartService.save(cartSaveParam);
    }

    @PostMapping("/list")
    public Result list(@RequestBody @Validated CartListParam cartListParam, BindingResult result) {

        if (result.hasErrors()) {
            return Result.fail("购物车数据查询失败");
        }
        return cartService.list(cartListParam.getUserId());
    }

    @PostMapping("/update")
    public Result update(@RequestBody Cart cart) {
        return cartService.update(cart);
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody Cart cart) {
        return cartService.remove(cart);
    }
}
