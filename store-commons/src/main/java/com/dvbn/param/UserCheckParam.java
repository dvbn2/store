package com.dvbn.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCheckParam {

    @NotBlank
    private String userName;
}
