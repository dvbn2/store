package com.dvbn.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关键搜索参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductSearchParams extends PageParam{
    private String search;
}
