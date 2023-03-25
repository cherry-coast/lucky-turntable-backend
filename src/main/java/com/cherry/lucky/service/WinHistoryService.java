package com.cherry.lucky.service;

import com.cherry.lucky.domain.CherryCommonPage;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.WinHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cherry.lucky.model.vo.WinHistoryVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @ClassName WinHistoryService
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月24日 17:27:00
 */

public interface WinHistoryService extends IService<WinHistory>{

    /**
     * 获取历史记录
     *
     * @param token
     * @param pageSize
     * @param pageNo
     * @return
     */
    CherryResponseEntity<CherryCommonPage<WinHistoryVO>> getHistory(String token, Integer pageSize, Integer pageNo) throws JsonProcessingException;

}
