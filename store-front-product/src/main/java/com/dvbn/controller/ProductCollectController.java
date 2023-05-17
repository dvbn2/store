package com.dvbn.controller;

import com.dvbn.param.ProductCollectParam;
import com.dvbn.service.ProductService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductCollectController {


    @Resource
    private ProductService productService;

    @PostMapping("/collect/list")
    public Result productIds(@RequestBody @Validated ProductCollectParam productCollectParam, BindingResult result) {

        if (result.hasErrors()) {
            return Result.fail("没有收藏数据");
        }
        return productService.ids(productCollectParam.getProductIds());
    }
}
