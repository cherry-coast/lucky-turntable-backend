package com.cherry.lucky.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WeChatUserInfoDTO
 * @Description 微信用户信息
 * @createTime 2023年03月22日 16:01:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatUserInfoWebRequestDTO {

    /**
     * 微信返回的code
     */
    @ApiModelProperty(value="微信返回的code")
    private String code;
    /**
     * 非敏感的用户信息
     */
    @ApiModelProperty(value="非敏感的用户信息")
    private String rawData;
    /**
     * 签名信息
     */
    @ApiModelProperty(value="签名信息")
    private String signature;
    /**
     * 加密的数据
     */
    @ApiModelProperty(value="加密的数据")
    private String encryptedData;
    /**
     * 加密密钥
     */
    @ApiModelProperty(value="加密密钥")
    private String iv;

}
