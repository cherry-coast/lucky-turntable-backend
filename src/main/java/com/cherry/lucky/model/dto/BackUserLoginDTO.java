package com.cherry.lucky.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName BackUserLoginDTO
 * @Description
 * @createTime 2023年04月27日 17:17:00
 */
@Data
public class BackUserLoginDTO {

    @ApiModelProperty("登录名")
    private String name;

    @ApiModelProperty("登录密码")
    private String password;

}
