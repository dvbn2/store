package com.dvbn.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 地址收集信息
 */
@Data
public class AddressListParam {

    @JsonProperty("user_id")
    private Integer userId;
}
