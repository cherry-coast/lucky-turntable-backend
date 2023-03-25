package com.cherry.lucky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName WinHistory
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月24日 17:27:00
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "lucky_win_history")
public class WinHistory {
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
     * 中奖时间
     */
    @TableField(value = "WINNING_DATE")
    private Date winningDate;

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