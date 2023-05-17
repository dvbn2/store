package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.clients.ProductClient;
import com.dvbn.mapper.CollectMapper;
import com.dvbn.param.ProductCollectParam;
import com.dvbn.pojo.Collect;
import com.dvbn.service.CollectService;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CollectServiceImpl implements CollectService {


    @Resource
    private CollectMapper collectMapper;

    @Resource
    private ProductClient productClient;

    /**
     * 收藏添加的方法
     *
     * @param collect
     * @return
     */
    @Override
    public Result save(Collect collect) {

        // 1.先查询是否存在
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", collect.getUserId())
                .eq("product_id", collect.getProductId());

        Long count = collectMapper.selectCount(queryWrapper);

        if (count > 0) {
            return Result.fail("收藏已经添加，无需再次添加");
        }
        // 2.不存在进行添加
        collect.setCollectTime(System.currentTimeMillis());
        int rows = collectMapper.insert(collect);
        log.info("CollectServiceImpl.save业务结束，结果:{}", rows);
        return Result.ok("收藏添加成功");
    }

    /**
     * 根据用户id查询商品信息集合
     *
     * @param userId
     * @return
     */
    @Override
    public Result list(Integer userId) {

        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.select("product_id");

        List<Object> idsObject = collectMapper.selectObjs(queryWrapper);
        ProductCollectParam productCollectParam = new ProductCollectParam();
        List<Integer> ids = new ArrayList<>();
        for (Object o : idsObject) {
            ids.add((Integer) o);
        }
        productCollectParam.setProductIds(ids);

        return productClient.productIds(productCollectParam);
    }

    /**
     * 根据用户id删除收藏
     *
     * @param collect
     * @return
     */
    @Override
    public Result remove(Collect collect) {

        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", collect.getUserId());
        queryWrapper.eq("product_id", collect.getProductId());

        int rows = collectMapper.delete(queryWrapper);
        return Result.ok("收藏移除成功");
    }
}