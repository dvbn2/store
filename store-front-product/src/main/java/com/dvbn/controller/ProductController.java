package com.dvbn.controller;

import com.dvbn.param.*;
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
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/promo")
    public Result promo(@RequestBody @Validated ProductPromoParam productPromoParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数错误,查询失败");
        }
        return productService.promo(productPromoParam.getCategoryName());
    }

    @PostMapping("/hots")
    public Result hots(@RequestBody @Validated ProductHotParam productPromoParam, BindingResult result) {

        if (result.hasErrors()) {
            return Result.fail("参数错误,查询失败");
        }
        return productService.hots(productPromoParam);
    }

    @PostMapping("category/list")
    public Result categoryList() {
        return productService.categoryList();
    }

    @PostMapping("/bycategory")
    public Result byCategory(@RequestBody @Validated ProductIdsParam productIdsParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("类别商品查询失败");
        }
        return productService.byCategory(productIdsParam);
    }

    @PostMapping("/all")
    public Result all(@RequestBody @Validated ProductIdsParam productIdsParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("类别商品查询失败");
        }
        return productService.byCategory(productIdsParam);
    }

    @PostMapping("/detail")
    public Result detail(@RequestBody @Validated ProductIdParam productIdParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("商品详情查询失败");
        }
        return productService.detail(productIdParam.getProductID());
    }

    @PostMapping("/pictures")
    public Result pictures(@RequestBody @Validated ProductIdParam productIdParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("商品图片详情查询失败");
        }
        return productService.pictures(productIdParam.getProductID());
    }

    @PostMapping("/search")
    public Result search(@RequestBody ProductSearchParams productSearchParams) {
        return productService.search(productSearchParams);
    }
}
