package com.dvbn.controller;

import com.dvbn.param.ProductCollectParam;
import com.dvbn.param.ProductIdParam;
import com.dvbn.pojo.Product;
import com.dvbn.service.ProductService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductCartController {


    @Resource
    private ProductService productService;

    @PostMapping("/cart/detail")
    public Product cartDetail(@RequestBody @Validated ProductIdParam productIdParam, BindingResult result) {

        if (result.hasErrors()) {
            return null;
        }
        Result detail = productService.detail(productIdParam.getProductID());


        return (Product) detail.getData();
    }

    @PostMapping("/cart/list")
    public List<Product> cartList(@RequestBody @Validated ProductCollectParam productCollectParam, BindingResult result) {

        if (result.hasErrors()) {
            return new ArrayList<>();
        }
        return productService.cartList(productCollectParam.getProductIds());
    }
}