package com.dvbn.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProductCollectParam {

    @NotEmpty
    private List<Integer> productIds;
}
