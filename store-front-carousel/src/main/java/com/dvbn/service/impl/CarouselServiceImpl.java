package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.mapper.CarouselMapper;
import com.dvbn.pojo.Carousel;
import com.dvbn.service.CarouselService;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {


    @Resource
    private CarouselMapper carouselMapper;


    /**
     * 查询优先级最高的6条数据
     *
     * @return
     */
    @Cacheable(value = "list.carousel", key = "#root.methodName", cacheManager = "cacheManagerDay")
    @Override
    public Result list() {
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("priority");
        List<Carousel> carousels = carouselMapper.selectList(queryWrapper);

        // stream流集合操作
        List<Carousel> list = carousels.stream().limit(6).collect(Collectors.toList());

        return Result.ok(list   );
    }
}
