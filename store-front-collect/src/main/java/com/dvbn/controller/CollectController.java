package com.dvbn.controller;

import com.dvbn.pojo.Collect;
import com.dvbn.service.CollectService;
import com.dvbn.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/save")
    public Result save(@RequestBody Collect collect) {
        return collectService.save(collect);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Collect collect) {

        return collectService.list(collect.getUserId());
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody Collect collect) {

        return collectService.remove(collect);
    }
}
