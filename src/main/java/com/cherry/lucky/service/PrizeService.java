package com.cherry.lucky.service;

import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.Prize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cherry.lucky.model.vo.PrizeVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName PrizeService
 * @Description
 * @createTime 2023年03月23日 11:10:00
 */

public interface PrizeService extends IService<Prize> {

    /**
     * 获取奖品
     * @param token token
     * @return {@link PrizeVO}
     */
    CherryResponseEntity<List<PrizeVO>> getPrize(String token) throws JsonProcessingException;

    /**
     * 抽奖
     * @param token token
     * @return 奖品名字
     */
    CherryResponseEntity<Integer> lottery(String token) throws JsonProcessingException;
}
