package com.cherry.lucky.controller;

import com.cherry.lucky.common.annotate.WebMonitorAnnotation;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName MonitorController
 * @Description 后期全改成 websocket 长连接
 * @createTime 2023年05月19日 10:52:00
 */
@RestController
@RequestMapping("/api/v1/monitor")
@Api(value = "监控api", tags = { "监控接口" })
public class MonitorController {

    private MonitorService monitorServiceImpl;

    @Autowired
    public void setMonitorServiceImpl(MonitorService monitorServiceImpl) {
        this.monitorServiceImpl = monitorServiceImpl;
    }


    /**
     * cpu 监控
     */
    @PostMapping("/cpu")
    @ApiOperation("cpu 监控")
    @WebMonitorAnnotation
    public CherryResponseEntity<BigDecimal> cpu() {
        return monitorServiceImpl.cpu();
    }



    /**
     * 网络速率 监控
     */
    @PostMapping("/networkRate")
    @ApiOperation("网络速率 监控")
    @WebMonitorAnnotation
    public CherryResponseEntity<BigDecimal> networkRate() {
        return monitorServiceImpl.networkRate();
    }


    /**
     * 系统内存 监控
     */
    @PostMapping("/memory")
    @ApiOperation("系统内存 监控")
    @WebMonitorAnnotation
    public CherryResponseEntity<Map<String, Object>> memory() {
        return monitorServiceImpl.memory();
    }

    /**
     * 磁盘使用量 监控
     */
    @PostMapping("/disk")
    @ApiOperation("磁盘使用量 监控")
    @WebMonitorAnnotation
    public CherryResponseEntity<BigDecimal> disk() {
        return monitorServiceImpl.disk();
    }


}
