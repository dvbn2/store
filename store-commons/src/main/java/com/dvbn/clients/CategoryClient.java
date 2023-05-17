package com.dvbn.clients;

import com.dvbn.param.ProductHotParam;
import com.dvbn.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("category-service") // 指定服务名称
public interface CategoryClient {


    @GetMapping("/category/promo/{categoryName}")
    Result byName(@PathVariable("categoryName") String categoryName);

    @PostMapping("/category/hots")
    Result hotsCategory(@RequestBody ProductHotParam productHotParam);

    @GetMapping("/category/list")
    Result list();
}
