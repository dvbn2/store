package com.dvbn.serivce;

import com.dvbn.param.ProductSearchParams;
import com.dvbn.utils.Result;

public interface SearchService {

    /**
     * 根据关键字和分页进行数据库数据查训
     * @param productSearchParams
     * @return
     */
    Result search(ProductSearchParams productSearchParams);
}
