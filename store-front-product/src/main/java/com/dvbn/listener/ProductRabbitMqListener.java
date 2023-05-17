package com.dvbn.listener;

import com.dvbn.service.ProductService;
import com.dvbn.to.OrderToProduct;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProductRabbitMqListener {

    @Resource
    private ProductService productService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "sub.queue"),
                    exchange = @Exchange("topic.ex"),
                    key = "sun.number"
            )
    )
    public void sunNumber(List<OrderToProduct> orderToProductList) {

        productService.subNumber(orderToProductList);
    }
}
