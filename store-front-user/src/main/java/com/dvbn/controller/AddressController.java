package com.dvbn.controller;

import com.dvbn.param.AddressListParam;
import com.dvbn.param.AddressParam;
import com.dvbn.param.AddressRemoveParam;
import com.dvbn.pojo.Address;
import com.dvbn.service.AddressService;
import com.dvbn.utils.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @PostMapping("/list")
    public Result list(@RequestBody @Validated AddressListParam addressListParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数异常，查询失败");
        }
        return addressService.list(addressListParam.getUserId());
    }

    @PostMapping("/save")
    public Result save(@RequestBody @Validated AddressParam addressParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数异常,保存失败");
        }
        Address address = addressParam.getAdd();
        address.setUserId(addressParam.getUserId());
        return addressService.save(address);
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody @Validated AddressRemoveParam addressRemoveParam, BindingResult result) {
        if (result.hasErrors()) {
            return Result.fail("参数异常,删除失败");
        }
        return addressService.remove(addressRemoveParam.getId());
    }
}
