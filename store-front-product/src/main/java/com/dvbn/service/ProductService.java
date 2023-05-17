package com.dvbn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dvbn.param.*;
import com.dvbn.pojo.Product;
import com.dvbn.to.OrderToProduct;
import com.dvbn.utils.Result;

import java.util.List;

public interface ProductService extends IService<Product> {

    /**
     * 根据单类别名称 查询热门商品
     *
     * @param categoryName
     * @return
     */
    Result promo(String categoryName);


    /**
     * 多类别热门商品查询根据类别名称集合!至多查询7条!
     *
     * @param productPromoParam
     * @return
     */
    Result hots(ProductHotParam productPromoParam);

    /**
     * 查询类别商品集合
     *
     * @return
     */
    Result categoryList();

    /**
     * 通用性业务
     * 传入了类别id,根据id查询并且分页
     * 没有传入类别的id!查询全部!
     *
     * @param productIdsParam
     * @return
     */
    Result byCategory(ProductIdsParam productIdsParam);

    /**
     * 根据商品id查询商品详情
     *
     * @param productID
     * @return
     */
    Result detail(Integer productID);

    /**
     * 查询商品图片详情
     *
     * @param productID
     * @return
     */
    Result pictures(Integer productID);

    /**
     * 查询所有商品信息
     *
     * @return
     */
    List<Product> allList();

    /**
     * 搜索业务，调用搜索服务
     *
     * @param productSearchParams
     * @return
     */
    Result search(ProductSearchParams productSearchParams);

    /**
     * 根据商品id集合查询商品信息
     *
     * @param productIds
     * @return
     */
    Result ids(List<Integer> productIds);

    /**
     * 根据商品id擦汗寻商品id集合
     *
     * @param productIds
     * @return
     */
    List<Product> cartList(List<Integer> productIds);

    /**
     * 修改库存和增加销售量
     *
     * @param orderToProductList
     */
    void subNumber(List<OrderToProduct> orderToProductList);
}
