package com.cherry.lucky.controller;

import com.cherry.lucky.common.annotate.WxInterfaceAnnotation;
import com.cherry.lucky.domain.BaseController;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.model.vo.PrizeVO;
import com.cherry.lucky.service.PrizeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName PrizeController
 * @Description
 * @createTime 2023年03月23日 11:11:00
 */
@RestController
@RequestMapping("/api/v1/prize")
@Api(value = "奖品api", tags = { "奖品接口" })
public class PrizeController extends BaseController {

    private PrizeService prizeServiceImpl;

    @Autowired
    public void setPrizeServiceImpl(PrizeService prizeServiceImpl) {
        this.prizeServiceImpl = prizeServiceImpl;
    }

    @GetMapping("/getPrize")
    @ApiOperation("获取当前token用户的奖品")
    @WxInterfaceAnnotation("getPrize")
    public CherryResponseEntity<List<PrizeVO>> getPrize() throws JsonProcessingException {
        return prizeServiceImpl.getPrize(getCurrentToken());
    }

    @GetMapping("/lottery")
    @ApiOperation("当前token用户抽奖")
    @WxInterfaceAnnotation("lottery")
    public CherryResponseEntity<Integer> lottery() throws JsonProcessingException {
        return prizeServiceImpl.lottery(getCurrentToken());
    }
}
