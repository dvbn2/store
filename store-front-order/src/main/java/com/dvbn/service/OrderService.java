package com.dvbn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dvbn.param.OrderParam;
import com.dvbn.pojo.Order;
import com.dvbn.utils.Result;

public interface OrderService extends IService<Order> {

    /**
     * 订单数据保存
     *
     * @param orderParam
     * @return
     */
    Result save(OrderParam orderParam);

    /**
     * 分组查询订单数据
     *
     * @param userId
     * @return
     */
    Result list(Integer userId);
}
