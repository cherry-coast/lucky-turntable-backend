package com.cherry.lucky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
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
@TableName(value = "lucky_prize")
public class Prize {
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 微信唯一标识
     */
    @TableField(value = "OPEN_ID")
    private String openId;

    /**
     * 奖品名称
     */
    @TableField(value = "PRIZE_NAME")
    private String prizeName;

    /**
     * 奖品图片
     */
    @TableField(value = "PRIZE_IMAGE")
    private String prizeImage;

    /**
     * 奖品概率
     */
    @TableField(value = "PRIZE_PROBABILITY")
    private Double prizeProbability;

    /**
     * 奖品数量
     */
    @TableField(value = "PRIZE_NUM")
    private Integer prizeNum;

    /**
     * 奖品数量单位
     */
    @TableField(value = "PRIZE_UNIT")
    private String prizeUnit;

    /**
     * 插入时间
     */
    @TableField(value = "INSERT_DATA")
    private Date insertData;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_DATA")
    private Date updateData;

    /**
     * 是否删除,0:否,1:是,默认否
     */
    @TableField(value = "DEL")
    private Boolean del;
}