package com.cherry.lucky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cherry.lucky.common.auth.UserInfo;
import com.cherry.lucky.constant.TokenConstants;
import com.cherry.lucky.domain.CherryCommonPage;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.model.vo.WinHistoryVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cherry.lucky.entity.WinHistory;
import com.cherry.lucky.mapper.WinHistoryMapper;
import com.cherry.lucky.service.WinHistoryService;
/**
 * @ClassName WinHistoryServiceImpl
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月24日 17:27:00
 */

@Service
public class WinHistoryServiceImpl extends ServiceImpl<WinHistoryMapper, WinHistory> implements WinHistoryService{
    private UserInfo userInfo;

    @Autowired
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public CherryResponseEntity<CherryCommonPage<WinHistoryVO>> getHistory(String token, Integer pageSize, Integer pageNo) throws JsonProcessingException {
        String openId = userInfo.getOpenIdByRedis(token);
        PageHelper.startPage(pageNo, pageSize);
        List<WinHistory> winHistoryList = this.baseMapper.selectList(new QueryWrapper<WinHistory>().eq("OPEN_ID", openId));
        CherryCommonPage<WinHistory> sourcePage = CherryCommonPage.restPage(winHistoryList);

        CherryCommonPage<WinHistoryVO> page =
                CherryCommonPage.transferPageData(sourcePage, WinHistoryVO.class);
        return CherryResponseEntity.success(page);
    }
}
