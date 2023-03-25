package com.cherry.lucky.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class WinHistoryVO {

    private Long id;

    /**
     * 奖品名称
     */
    private String prizeName;


    /**
     * 中奖时间
     */
    private Date winningDate;

}