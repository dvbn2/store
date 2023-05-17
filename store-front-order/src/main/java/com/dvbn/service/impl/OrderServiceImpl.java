package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dvbn.clients.ProductClient;
import com.dvbn.mapper.OrderMapper;
import com.dvbn.param.OrderParam;
import com.dvbn.param.ProductCollectParam;
import com.dvbn.pojo.Order;
import com.dvbn.pojo.Product;
import com.dvbn.service.OrderService;
import com.dvbn.to.OrderToProduct;
import com.dvbn.utils.Result;
import com.dvbn.vo.CartVo;
import com.dvbn.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Resource
    private ProductClient productClient;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 订单数据保存
     * 1.将购物车数据转成订单数据
     * 2.进行订单数据的批量插入
     * 3.商品库存修改消息
     * 4.发送购物车库存修改消息
     *
     * @param orderParam
     * @return
     */
    @Override
    @Transactional
    public Result save(OrderParam orderParam) {

        //准备数据
        List<Integer> cartIds = new ArrayList<>();
        List<OrderToProduct> orderToProducts = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        // 生成数据
        Integer userId = orderParam.getUserId();
        long orderId = System.currentTimeMillis();

        for (CartVo cartVo : orderParam.getProducts()) {
            cartIds.add(cartVo.getId()); //保存删除的购物车项的id
            OrderToProduct orderToProduct = new OrderToProduct();

            orderToProduct.setNum(cartVo.getNum());
            orderToProduct.setProductId(cartVo.getProductID());
            orderToProducts.add(orderToProduct);//商品服务修改的数据

            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderTime(orderId);
            order.setUserId(userId);
            order.setProductId(cartVo.getProductID());
            order.setProductNum(cartVo.getNum());
            order.setProductPrice(cartVo.getPrice());
            orderList.add(order);
        }

        // 订单数据批量保存
        saveBatch(orderList);

        //发送购物车消息
        rabbitTemplate.convertAndSend("topic.ex", "clear.cart", cartIds);

        //发送商品服 务消息
        rabbitTemplate.convertAndSend("topic.ex", "sub.cart", orderToProducts);

        return Result.ok("订单保存成功");
    }

    /**
     * 分组查询订单数据
     * 1.查询用户对应的全部订单项
     * 2.利用stream进行订单分组orderId
     * 3.查询订单的全部商品集合,并使用stream组成map
     * 4.封装返回的OrderVo对象
     *
     * @param userId
     * @return
     */
    @Override
    public Result list(Integer userId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Order> list = list(queryWrapper);

        // 将orderId相同的订单添加到一个集合中作为value
        Map<Long, List<Order>> orderMap = list.stream().collect(Collectors.groupingBy(Order::getOrderId));

        // 查询商品数据
        List<Integer> productIds = list.stream().map(Order::getProductId).collect(Collectors.toList());

        ProductCollectParam productCollectParam = new ProductCollectParam();
        productCollectParam.setProductIds(productIds);

        List<Product> products = productClient.cartList(productCollectParam);

        Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, v -> v));

        // 结果封装
        List<List<OrderVo>> result = new ArrayList<>();

        //遍历订单项集合
        for (List<Order> orders : orderMap.values()) {
            //封装每一个订单
            List<OrderVo> orderVos = new ArrayList<>();
            for (Order order : orders) {
                OrderVo orderVo = new OrderVo();
                BeanUtils.copyProperties(order, orderVo);
                Product product = productMap.get(order.getProductId());
                orderVo.setProductName(product.getProductName());
                orderVo.setProductPicture(product.getProductPicture());
                orderVos.add(orderVo);
            }
            result.add(orderVos);
        }
        Result ok = Result.ok("订单数据获取成功", result);
        log.info("orderServiceImpl .list业务结束，结果:{}", ok);

        return ok;
    }
}
