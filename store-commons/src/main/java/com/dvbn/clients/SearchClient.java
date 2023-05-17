package com.dvbn.clients;

import com.dvbn.param.ProductSearchParams;
import com.dvbn.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("search-service")
public interface SearchClient {

    @PostMapping("/search/product")
    Result search(@RequestBody ProductSearchParams productSearchParams);
}
