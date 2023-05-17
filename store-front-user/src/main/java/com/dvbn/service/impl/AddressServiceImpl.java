package com.dvbn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dvbn.mapper.AddressMapper;
import com.dvbn.pojo.Address;
import com.dvbn.service.AddressService;
import com.dvbn.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {


    @Resource
    private AddressMapper addressMapper;


    @Override
    public Result list(Integer userId) {
        // 1.封装查询参数
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Address> list = addressMapper.selectList(queryWrapper);
        // 2.封装结果
        return Result.ok("查询成功", list);
    }

    @Override
    public Result save(Address address) {
        // 1.插入数据
        int rows = addressMapper.insert(address);

        if (rows == 0) {
            return Result.fail("添加地址失败");
        }
        // 2.返回查询结果
        return list(address.getUserId());
    }

    @Override
    public Result remove(Integer id) {
        int rows = addressMapper.deleteById(id);
        if (rows == 0) {
            return Result.fail("删除失败");
        }
        return Result.ok("删除成功");
    }
}
