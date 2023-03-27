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
 * @ClassName LuckyInterfaceLog
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月27日 10:42:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "lucky_interface_log")
public class InterfaceLog {
    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private Integer id;

    /**
     * 操作描述
     */
    @TableField(value = "DESCRIPTION")
    private String description;

    /**
     * 操作用户
     */
    @TableField(value = "USERNAME")
    private String username;

    /**
     * 消耗时间
     */
    @TableField(value = "SPEND_TIME")
    private Long spendTime;

    /**
     * 根路径
     */
    @TableField(value = "BASE_PATH")
    private String basePath;

    /**
     * URI
     */
    @TableField(value = "URI")
    private String uri;

    /**
     * URL
     */
    @TableField(value = "URL")
    private String url;

    /**
     * 请求类型
     */
    @TableField(value = "`METHOD`")
    private String method;

    /**
     * IP地址
     */
    @TableField(value = "IP")
    private String ip;

    /**
     * 请求参数
     */
    @TableField(value = "`PARAMETER`")
    private String parameter;

    /**
     * 返回结果
     */
    @TableField(value = "`RESULT`")
    private String result;

    /**
     * 开始时间
     */
    @TableField(value = "RECORD_TIME")
    private Long recordTime;

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