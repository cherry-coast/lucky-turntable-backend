package com.cherry.lucky.entity;

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
 * @ClassName BackUser
 * @author cherry
 * @version 1.0.0
 * @Description 后台用户
 * @createTime 2023年04月27日 16:55:00
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "lucky_back_user")
public class BackUser {
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 名字
     */
    @TableField(value = "`NAME`")
    private String name;

    /**
     * 密码
     */
    @TableField(value = "`PASSWORD`")
    private String password;

    /**
     * 上次登录时间
     */
    @TableField(value = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

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