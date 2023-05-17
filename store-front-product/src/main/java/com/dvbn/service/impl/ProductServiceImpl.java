package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvbn.clients.CategoryClient;
import com.dvbn.clients.SearchClient;
import com.dvbn.mapper.PictureMapper;
import com.dvbn.mapper.ProductMapper;

import com.dvbn.param.ProductHotParam;


import com.dvbn.param.ProductIdsParam;
import com.dvbn.param.ProductSearchParams;
import com.dvbn.pojo.Picture;
import com.dvbn.pojo.Product;
import com.dvbn.service.ProductService;
import com.dvbn.to.OrderToProduct;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryClient categoryClient;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private SearchClient searchClient;

    /**
     * 单类别名称查询热门商品至多7条数据
     * 1.根据类别名称调用feign客户端访问类别服务获取类别的数据
     * 2.成功继续根据类别id查询商品数据 [热门 销售量倒序 查询7]
     * 3.结果封装即可
     *
     * @param categoryName
     * @return
     */
    @Cacheable(value = "list.product", key = "#categoryName", cacheManager = "cacheManagerDay")
    @Override
    public Result promo(String categoryName) {

        Result result = categoryClient.byName(categoryName);

        if (result.getCode().equals(Result.FAIL_CODE)) {
            log.info("ProductServiceImpl. promo业务结束，结果:{}", "类别查询失败!");
            return result;
        }

        //类别服务中data = category --- feign {json} ----- product服务 LinkedHashMap jackson

        /*Category category = (Category) result.getData();
        Integer categoryId = category.getCategoryId();*/

        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) result.getData();
        Integer categoryId = (Integer) map.get("category_id");


        // 封装查询参数
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                .orderByDesc("product_sales");
        IPage<Product> page = new Page<>(1, 7);

        page = productMapper.selectPage(page, queryWrapper);
        List<Product> records = page.getRecords();

        log.info("ProductServiceImpl. promo业务结束， 结果:{}", records);
        return Result.ok("数据查询成功", records);
    }


    /**
     * 多类别热门商品查询根据类别名称集合!至多查询7条!
     * <p>
     * 1.调用类别服务
     * 2.类别集合id查询商品
     * 3.结果集封装即可
     *
     * @param productPromoParam
     * @return
     */
    @Cacheable(value = "list.product", key = "#productPromoParam.categoryName", cacheManager = "cacheManagerHour")
    @Override
    public Result hots(ProductHotParam productPromoParam) {

        Result result = categoryClient.hotsCategory(productPromoParam);

        if (result.getCode().equals(Result.FAIL_CODE)) {
            log.info("ProductServiceImpl.hots业务结束，结果:{}", result.getMsg());
            return result;
        }

        List<Object> ids = (List<Object>) result.getData();

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_id", ids)
                .orderByDesc("product_sales");

        IPage<Product> page = new Page<>(1, 7);

        page = productMapper.selectPage(page, queryWrapper);

        List<Product> records = page.getRecords();

        return Result.ok("多类别热门商品查询成功", records);
    }

    /**
     * 查询类别商品集合
     *
     * @return
     */
    // 没有参数使用#root：当前方法对象 #root.methodName：获取方法名
    @Cacheable(value = "list.category", key = "#root.methodName", cacheManager = "cacheManagerDay")
    @Override
    public Result categoryList() {
        return categoryClient.list();
    }

    /**
     * 通用性业务
     * 传入了类别id,根据id查询并且分页
     * 没有传入类别的id!查询全部!
     *
     * @param productIdsParam
     * @return
     */
    @Cacheable(value = "list.product",
            key = "#productIdsParam.categoryID+'-'+" +
                    "#productIdsParam.currentPage+'-'+" +
                    "#productIdsParam.pageSize") // 不设置cacheManager默认使用CacheConfiguration中的第一个方法
    @Override
    public Result byCategory(ProductIdsParam productIdsParam) {
        List<Integer> categoryID = productIdsParam.getCategoryID();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        if (!categoryID.isEmpty()) {
            queryWrapper.in("category_id", categoryID);
        }
        Page<Product> page = new Page<>(productIdsParam.getCurrentPage(), productIdsParam.getPageSize());

        page = productMapper.selectPage(page, queryWrapper);

        // 结果封装
        List<Product> records = page.getRecords();
        long total = page.getTotal();
        return Result.ok("查询成功", records, total);
    }

    /**
     * 根据商品id查询商品详情
     *
     * @param productID
     * @return
     */
    @Cacheable(value = "product", key = "#productID")
    @Override
    public Result detail(Integer productID) {
        Product product = productMapper.selectById(productID);
        log.info("ProductServiceImpl.detail业务结束，结果:{}", product);
        return Result.ok(product);
    }

    /**
     * 查询商品图片详情
     *
     * @param productID
     * @return
     */
    @Cacheable(value = "picture", key = "#productID")
    @Override
    public Result pictures(Integer productID) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productID);

        List<Picture> pictureList = pictureMapper.selectList(queryWrapper);
        log.info(" ProductServiceImpl.pictures业务结束，结果:{}", pictureList);
        return Result.ok(pictureList);
    }

    /**
     * 查询所有商品信息
     * 数据同步
     *
     * @return
     */
    @Override
    public List<Product> allList() {
        return productMapper.selectList(null);
    }

    /**
     * 搜索业务。需要调用搜索服务!
     *
     * @param productSearchParams
     * @return
     */
    @Override
    public Result search(ProductSearchParams productSearchParams) {
        Result result = searchClient.search(productSearchParams);
        log.info("ProductServiceImpl.search业务结束，结果:{}", result);
        return result;
    }

    /**
     * 根据商品id集合查询商品信息
     *
     * @param productIds
     * @return
     */
    @Cacheable(value = "list.product", key = "#productIds")
    @Override
    public Result ids(List<Integer> productIds) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("product_id", productIds);

        List<Product> products = productMapper.selectList(queryWrapper);

        return Result.ok("类别信息查询成功", products);
    }

    /**
     * 根据商品id,查询商品id集合
     *
     * @param productIds
     * @return
     */
    @Override
    public List<Product> cartList(List<Integer> productIds) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("product_id", productIds);

        return productMapper.selectList(queryWrapper);
    }

    /**
     * 修改库存和增加销售量
     *
     * @param orderToProductList
     */
    @Transactional
    @Override
    public void subNumber(List<OrderToProduct> orderToProductList) {

        //将集合转成map   key:productId   value:orderToProduct

        Map<Integer, OrderToProduct> map = orderToProductList.stream().collect(Collectors.toMap(OrderToProduct::getProductId, v -> v));

        //获取商品的id集合
        Set<Integer> productIds = map.keySet();

        //查询集合对应的商品信息
        List<Product> products = productMapper.selectBatchIds(productIds);

        //修改商品信息
        for (Product product : products) {
            Integer num = map.get(product.getProductId()).getNum();
            product.setProductNum(product.getProductNum() - num);
            product.setProductSales(product.getProductSales() + num);
        }

        //批量更新
        updateBatchById(products);
        log.info(" ProductServiceImpl.subNumber业务结束，结果:库存和销售量的修改完毕");
    }
}
