package com.dvbn.controller;

import com.dvbn.pojo.Product;
import com.dvbn.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductSearchController {


    @Resource
    private ProductService productService;

    @GetMapping("/list")
    public List<Product> allList() {
        return productService.allList();
    }
}
