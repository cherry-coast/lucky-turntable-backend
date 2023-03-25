package com.cherry.lucky.controller;

import com.cherry.lucky.common.annotate.WxInterfaceAnnotation;
import com.cherry.lucky.domain.BaseController;
import com.cherry.lucky.domain.CherryCommonPage;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.WinHistory;
import com.cherry.lucky.model.vo.PrizeVO;
import com.cherry.lucky.model.vo.WinHistoryVO;
import com.cherry.lucky.service.PrizeService;
import com.cherry.lucky.service.WinHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WinHistoryController
 * @Description
 * @createTime 2023年03月23日 11:11:00
 */
@RestController
@RequestMapping("/api/v1/win/history")
@Api(value = "抽奖记录api", tags = { "抽奖记录接口" })
public class WinHistoryController extends BaseController {

    private WinHistoryService winHistoryServiceImpl;

    @Autowired
    public void setWinHistoryServiceImpl(WinHistoryService winHistoryServiceImpl) {
        this.winHistoryServiceImpl = winHistoryServiceImpl;
    }

    @GetMapping("/getHistory")
    @ApiOperation("获取当前token用户的抽奖记录")
    @WxInterfaceAnnotation("getHistory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页总条数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNo", value = "页码", dataType = "Integer")
    })
    public CherryResponseEntity<CherryCommonPage<WinHistoryVO>> getHistory(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNo
    ) throws JsonProcessingException {
        return winHistoryServiceImpl.getHistory(getCurrentToken(), pageSize, pageNo);
    }

}
