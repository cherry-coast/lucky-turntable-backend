package com.cherry.lucky.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName RedisUserInfo
 * @Description
 * @createTime 2023年03月25日 14:56:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisUserInfo {

    private String username;

    private String openId;

    private Integer gender;

}
