package com.dvbn.controller;

import com.dvbn.param.ProductSearchParams;
import com.dvbn.serivce.SearchService;
import com.dvbn.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Resource
    private SearchService searchService;

    @PostMapping("/product")
    public Result searchProduct(@RequestBody ProductSearchParams productSearchParams) {
        return searchService.search(productSearchParams);
    }
}
