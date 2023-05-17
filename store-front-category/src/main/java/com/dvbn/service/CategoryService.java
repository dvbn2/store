package com.dvbn.service;

import com.dvbn.param.ProductHotParam;
import com.dvbn.utils.Result;

public interface CategoryService {

    /**
     * 根据类别名称，查询类别对象
     * @param categoryName
     * @return
     */
    Result byName(String categoryName);

    /**
     * 根据传入的热门类别名称集合!返回类别对应的id集合
     * @param productHotParam
     * @return
     */
    Result hotsCategory(ProductHotParam productHotParam);

    /**
     * 查询类别集合数据
     * @return
     */
    Result list();
}
