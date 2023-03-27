package com.cherry.lucky.service;

import com.cherry.lucky.entity.InterfaceLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName LuckyInterfaceLogService
 * @Description
 * @createTime 2023年03月27日 10:42:00
 */

public interface InterfaceLogService extends IService<InterfaceLog> {

    /**
     * 保存日志
     * @param interfaceLog 日志信息
     */
    void saveLog(com.cherry.lucky.domain.InterfaceLog interfaceLog);

}
