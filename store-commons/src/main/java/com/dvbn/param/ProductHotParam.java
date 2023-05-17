package com.dvbn.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProductHotParam {

    @NotEmpty // 集合使用@NotEmpty 字符串或对象使用@NotBlank
    private List<String> categoryName;
}
