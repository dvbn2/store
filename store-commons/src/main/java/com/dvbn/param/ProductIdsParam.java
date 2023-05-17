package com.dvbn.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品id集合
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductIdsParam extends PageParam{

    @NotNull
    private List<Integer> categoryID;
}
