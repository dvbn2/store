package com.dvbn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户pojo
 */
@TableName("user")
@Data
public class User implements Serializable {

    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @JsonProperty("user_id") // jackson的注解，用于进行属性格式化!
    private Integer userId;
    @Length(min = 6)  // 字符串长度最小为6
    private String userName;

    //忽略属性不生成json 不接受json数据 @JsonIgnore
    // 当这个值不为null的时候生成json,为null不生成 @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    @NotBlank
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userPhonenumber;
}
