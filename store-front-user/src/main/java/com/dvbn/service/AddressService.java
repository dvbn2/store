package com.dvbn.service;

import com.dvbn.pojo.Address;
import com.dvbn.utils.Result;

public interface AddressService {

    /**
     * 根据用户id查询地址数据
     * @param userId
     * @return
     */
    Result list(Integer userId);

    /**
     * 插入地址数据，插入成功后，要返回行的数据集合
     * @param address
     * @return
     */
    Result save(Address address);

    /**
     * 删除地址数据
     * @param id
     * @return
     */
    Result remove(Integer id);
}
