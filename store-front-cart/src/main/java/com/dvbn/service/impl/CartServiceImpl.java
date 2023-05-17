package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.clients.ProductClient;
import com.dvbn.mapper.CartMapper;
import com.dvbn.param.CartSaveParam;
import com.dvbn.param.ProductCollectParam;
import com.dvbn.param.ProductIdParam;
import com.dvbn.pojo.Cart;
import com.dvbn.pojo.Product;
import com.dvbn.service.CartService;
import com.dvbn.utils.Result;
import com.dvbn.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Resource
    private ProductClient productClient;
    @Resource
    private CartMapper cartMapper;

    /**
     * 根据商品id和用户id添加购物车
     * 001成功 002已经存在 003没有库有
     *
     * @param cartSaveParam
     * @return
     */
    @Override
    public Result save(CartSaveParam cartSaveParam) {

        //查询商品数据
        ProductIdParam productIdParam = new ProductIdParam();
        productIdParam.setProductID(cartSaveParam.getProductId());

        Product product = productClient.productDetail(productIdParam);

        //检查库存
        if (product == null) {
            return Result.fail("商品已经被删除，无法添加购物车");
        }
        if (product.getProductNum() == 0) {
            Result ok = Result.ok("没有库存，无法购买");
            ok.setCode("003");
            return ok;
        }

        //检查是否添加过
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cartSaveParam.getUserId());
        queryWrapper.eq("product_id", cartSaveParam.getProductId());

        Cart cart = cartMapper.selectOne(queryWrapper);
        if (cart != null) {
            //证明购物车存在!
            //原有的数量+1
            cart.setNum(cart.getNum() + 1);

            //返回002提示即可
            Result ok = Result.ok("购物车存在该商品，数量+1");
            ok.setCode("002");
            return ok;
        }

        //添加购物车
        cart = new Cart();
        cart.setNum(1);
        cart.setUserId(cartSaveParam.getUserId());
        cart.setProductId(cartSaveParam.getProductId());

        int rows = cartMapper.insert(cart);
        log.info("CartServiceImpl.save业务结束，结果:{}", rows);

        //结果封装和返回
        CartVo cartVo = new CartVo(product, cart);

        return Result.ok("购物车数据添加成功", cartVo);
    }


    /**
     * 返回购物车数据
     *
     * @param userId
     * @return
     */
    @Override
    public Result list(Integer userId) {

        //1.用户id查询购物车数据
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId);
        List<Cart> carts = cartMapper.selectList(queryWrapper);

        //2.判断是否存在,不存在,返回一个空集合!
        if (carts == null || carts.size() == 0) {

            carts = new ArrayList<>(); // 和前端做了规定，没有数据页必须返回口数据
            return Result.ok("购物车空空如也", carts);
        }

        //3.存在获取商品的id集合,并且调用商品服务查询
        ArrayList<Integer> productIds = new ArrayList<>();
        for (Cart cart : carts) {
            productIds.add(cart.getProductId());
        }
        ProductCollectParam productCollectParam = new ProductCollectParam();
        productCollectParam.setProductIds(productIds);
        List<Product> productList = productClient.cartList(productCollectParam);

        // 商品集合-商品map商品的id = key商品 = value
        //jdk 8 stream
        Map<Integer, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId, v -> v));

        //4.进行vo的封装
        ArrayList<CartVo> cartVoList = new ArrayList<>();
        for (Cart cart : carts) {
            CartVo cartVo = new CartVo(productMap.get(cart.getProductId()), cart);
            cartVoList.add(cartVo);
        }

        return Result.ok("查询成功", cartVoList);
    }

    /**
     * 购物车修改
     *
     * @param cart
     * @return
     */
    @Override
    public Result update(Cart cart) {

        ProductIdParam productIdParam = new ProductIdParam();
        productIdParam.setProductID(cart.getProductId());
        Product product = productClient.productDetail(productIdParam);

        // 判断库存
        if (cart.getNum() > product.getProductNum()) {
            log.info("CartServiceImpl . update业务结束，结果:{}", "修改失败! 库存不足!");
            return Result.fail("修改失败, 库存不足");
        }

        // 修改数据
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId())
                .eq("product_id", cart.getProductId());

        Cart newCart = cartMapper.selectOne(queryWrapper);

        newCart.setNum(cart.getNum());

        int rows = cartMapper.updateById(newCart);
        log.info("CartServiceImpl update业务结束，结果:{}", rows);

        return Result.ok("数量修改成功");
    }

    /**
     * 删除购物车
     *
     * @param cart
     * @return
     */
    @Override
    public Result remove(Cart cart) {

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId())
                .eq("product_id", cart.getProductId());

        int rows = cartMapper.delete(queryWrapper);
        log.info("CartServiceImpl update业务结束，结果:{}", rows);
        return Result.ok("删除成功");
    }

    /**
     * 清空对应id的购物车项
     *
     * @param cartIds
     */
    @Override
    public void clearIds(List<Integer> cartIds) {

        cartMapper.deleteBatchIds(cartIds);
        log.info("CartServiceImpl.clearIds业务结束，结果:{}", cartIds);
    }
}
