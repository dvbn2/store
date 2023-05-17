package com.dvbn.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    public static final Long serialVersionUID = 1L;

    public static final String SUCCESS_CODE = "001";
    /**
     * 失败状态码
     */
    public static final String FAIL_CODE = "004";
    /**
     * 未登录
     */
    public static final String USER_NO_LOGIN = "401";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public static Result ok() {
        return new Result(SUCCESS_CODE, null, null, null);
    }

    public static Result ok(Object data) {
        return new Result(SUCCESS_CODE, null, data, null);
    }

    public static Result ok(String msg, Object data) {
        return new Result(SUCCESS_CODE, msg, data, null);
    }

    public static Result ok(String msg, List<?> data, Long total) {
        return new Result(SUCCESS_CODE, msg, data, total);
    }

    public static Result fail(String msg) {
        return new Result(FAIL_CODE, msg, null, null);
    }
}
