package com.dvbn.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 购物车参数接收
 */
@Data
public class CartSaveParam implements Serializable {

    @JsonProperty("product_id")
    @NotNull
    private Integer productId;

    @JsonProperty("user_id")
    @NotNull
    private Integer userId;

}
