package com.dvbn.service;

import com.dvbn.param.UserCheckParam;
import com.dvbn.param.UserLoginParam;
import com.dvbn.pojo.User;
import com.dvbn.utils.Result;

public interface UserService {

    /**
     * 检查账号是否可用业务
     * @param userCheckParam 账号参数
     * @return
     */
    Result check(UserCheckParam userCheckParam);


    /**
     * 注册业务
     * @param user
     * @return
     */
    Result register(User user);

    Result login(UserLoginParam userLoginParam);
}
