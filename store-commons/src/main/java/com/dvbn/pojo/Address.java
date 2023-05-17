package com.dvbn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 地址pojo
 */
@Data
@TableName("address")
public class Address {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank
    private String address;
    @NotBlank
    private String linkman;
    @NotBlank
    private String phone;

    @TableField("user_id")
    private Integer userId;

}
