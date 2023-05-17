package com.dvbn.service;

import com.dvbn.param.CartSaveParam;
import com.dvbn.pojo.Cart;
import com.dvbn.utils.Result;

import java.util.List;

public interface CartService {

    /**
     * 根据商品id和用户id添加购物车
     * 001成功 002已经存在 003没有库有
     *
     * @param cartSaveParam
     * @return
     */
    Result save(CartSaveParam cartSaveParam);


    /**
     * 返回购物车数据
     *
     * @param userId
     * @return
     */
    Result list(Integer userId);

    /**
     * 购物车修改
     *
     * @param cart
     * @return
     */
    Result update(Cart cart);

    /**
     * 删除购物车
     *
     * @param cart
     * @return
     */
    Result remove(Cart cart);

    /**
     * 清空对应id的购物车项
     *
     * @param cartIds
     */
    void clearIds(List<Integer> cartIds);
}
