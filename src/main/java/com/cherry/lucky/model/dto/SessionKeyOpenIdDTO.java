package com.cherry.lucky.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName SessionKeyOpenIdDTO
 * @Description
 * @createTime 2023年03月22日 16:15:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionKeyOpenIdDTO {

    /**
     * openid
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * sessionKey
     */
    @JsonProperty("session_key")
    private String sessionKey;
}
