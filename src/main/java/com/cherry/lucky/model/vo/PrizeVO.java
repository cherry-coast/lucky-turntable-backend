package com.cherry.lucky.model.vo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PrizeDTO
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月23日 11:10:00
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrizeVO {
    private Long id;

    /**
     * 微信唯一标识
     */
    private String openId;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 奖品图片
     */
    private String prizeImage;

    /**
     * 奖品标题
     */
    private String prizeTitle;

    /**
     * 奖品数量
     */
    private Integer prizeNum;

    /**
     * 奖品数量单位
     */
    private String prizeUnit;

    /**
     * 插入时间
     */
    private Date insertData;

    /**
     * 更新时间
     */
    private Date updateData;

    /**
     * 是否删除,0:否,1:是,默认否
     */
    private Boolean del;
}