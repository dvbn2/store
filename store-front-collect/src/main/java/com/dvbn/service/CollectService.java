package com.dvbn.service;

import com.dvbn.pojo.Collect;
import com.dvbn.utils.Result;

public interface CollectService {

    /**
     * 收藏添加的方法
     *
     * @param collect
     * @return
     */
    Result save(Collect collect);

    /**
     * 根据用户id查询商品信息集合
     *
     * @param userId
     * @return
     */
    Result list(Integer userId);

    /**
     * 根据用户id删除收藏
     *
     * @param collect
     * @return
     */
    Result remove(Collect collect);
}
