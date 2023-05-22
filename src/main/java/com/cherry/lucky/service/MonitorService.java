package com.cherry.lucky.service;

import com.cherry.lucky.domain.CherryResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName MonitorService
 * @Description
 * @createTime 2023年05月19日 10:54:00
 */
public interface MonitorService {

    /**
     * cpu 监控
     *
     * @return cpu 占用
     */
    CherryResponseEntity<BigDecimal> cpu();

    /**
     * 网络速率 监控
     *
     * @return 网络速率
     */
    CherryResponseEntity<BigDecimal> networkRate();

    /**
     * 系统内存 监控
     *
     * @return 系统内存
     */
    CherryResponseEntity<Map<String, Object>> memory();

    /**
     * 磁盘使用量 监控
     *
     * @return 磁盘使用量
     */
    CherryResponseEntity<BigDecimal> disk();
}
