package com.dvbn.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dvbn.param.ProductHotParam;
import com.dvbn.service.CategoryService;
import com.dvbn.utils.Result;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/promo/{categoryName}")
    public Result byName(@PathVariable String categoryName) {
        if (StringUtils.isEmpty(categoryName)) {
            return Result.fail("参数错误,查询失败");
        }
        return categoryService.byName(categoryName);
    }

    /**
     * 根据id热门类别查询
     * @param productHotParam
     * @param result
     * @return
     */
    @PostMapping("/hots")
    public Result hotsCategory(@RequestBody @Validated ProductHotParam productHotParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数错误,查询失败");
        }
        return categoryService.hotsCategory(productHotParam);
    }

    /**
     * 查询类别集合数据
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        return categoryService.list();
    }
}
