package com.dvbn.param;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class PageParam {

    private int currentPage = 1;
    private int pageSize = 15;
}
