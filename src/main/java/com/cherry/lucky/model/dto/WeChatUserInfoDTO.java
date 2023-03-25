package com.cherry.lucky.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WeChatUserInfoDTO
 * @Description
 * @createTime 2023年03月22日 16:57:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatUserInfoDTO {

    private String nickName;

    private String avatarUrl;

    private String gender;

    private String city;

    private String country;

    private String province;

}
