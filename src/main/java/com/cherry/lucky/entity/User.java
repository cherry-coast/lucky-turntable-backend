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
 * @ClassName User
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月22日 16:43:00
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "lucky_user")
public class User {
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 微信昵称
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 微信标识
     */
    @TableField(value = "OPEN_ID")
    private String openId;

    /**
     * 性别
     */
    @TableField(value = "GENDER")
    private Integer gender;

    /**
     * 省
     */
    @TableField(value = "PROVINCE")
    private String province;

    /**
     * 国家
     */
    @TableField(value = "CITY")
    private String city;

    /**
     * 区
     */
    @TableField(value = "COUNTRY")
    private String country;

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