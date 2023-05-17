package com.dvbn.service;

import com.dvbn.utils.Result;

public interface CarouselService {

    /**
     * 查询优先级最高的6条数据
     * @return
     */
    Result list();
}
