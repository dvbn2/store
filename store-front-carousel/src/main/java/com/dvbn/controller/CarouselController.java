package com.dvbn.controller;

import com.dvbn.service.CarouselService;
import com.dvbn.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/carousel")
public class CarouselController {


    @Resource
    private CarouselService carouselService;

    /**
     * 查询轮播图数据
     * @return
     */
    @PostMapping("/list")
    public Result list() {
        return carouselService.list();
    }
}
