package com.cherry.lucky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cherry.lucky.common.auth.UserInfo;
import com.cherry.lucky.constant.TokenConstants;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.entity.WinHistory;
import com.cherry.lucky.model.vo.PrizeVO;
import com.cherry.lucky.service.WinHistoryService;
import com.cherry.lucky.utils.LotteryUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cherry.lucky.entity.Prize;
import com.cherry.lucky.mapper.PrizeMapper;
import com.cherry.lucky.service.PrizeService;
import org.springframework.util.CollectionUtils;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName PrizeServiceImpl
 * @Description
 * @createTime 2023年03月23日 11:10:00
 */

@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize> implements PrizeService {

    private WinHistoryService winHistoryServiceImpl;

    private UserInfo userInfo;

    @Autowired
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Autowired
    public void setWinHistoryServiceImpl(WinHistoryService winHistoryServiceImpl) {
        this.winHistoryServiceImpl = winHistoryServiceImpl;
    }

    private List<Prize> getPrizeOnDefault(String openId) {
        List<Prize> prizes = this.baseMapper.selectList(
                new QueryWrapper<Prize>()
                        .eq("DEL", 0)
                        .eq("OPEN_ID", openId)
                        .gt("PRIZE_NUM", 0)
                        .orderByAsc("ID")
        );
        if (CollectionUtils.isEmpty(prizes)) {
            prizes = this.baseMapper.selectList(
                    new QueryWrapper<Prize>()
                            .eq("DEL", 0)
                            .eq("OPEN_ID", TokenConstants.DEFAULT_OPEN_ID)
                            .gt("PRIZE_NUM", 0)
                            .orderByAsc("ID"));
        }
        return prizes;
    }

    @Override
    public CherryResponseEntity<List<PrizeVO>> getPrize(String token) throws JsonProcessingException {
        String openId = userInfo.getOpenIdByRedis(token);
        List<Prize> prizes = getPrizeOnDefault(openId);
        return CherryResponseEntity.success(prizes.stream().map(x -> {
            PrizeVO prizeDTO = new PrizeVO();
            BeanUtils.copyProperties(x, prizeDTO);
            return prizeDTO;
        }).collect(Collectors.toList()));
    }

    @Override
    public CherryResponseEntity<Integer> lottery(String token) throws JsonProcessingException {
        String openId = userInfo.getOpenIdByRedis(token);
        List<Prize> prizes = getPrizeOnDefault(openId);

        List<Prize> validPrizes = prizes.stream().filter(x -> x.getPrizeNum() > 0).toList();

        LotteryUtil lotteryUtil = new LotteryUtil(
                validPrizes.stream().map(Prize::getPrizeProbability).collect(Collectors.toList())
        );

        Prize prize = validPrizes.get(lotteryUtil.getPrizeIndex());

        int index = 0;
        for (int i = 0; i < prizes.size(); i++) {
            if (prizes.get(i).getId().equals(prize.getId())) {
                winHistoryServiceImpl.save(
                        WinHistory.builder().prizeName(prizes.get(i).getPrizeName()).openId(openId).build()
                );
                index = i;
            }
        }
        return CherryResponseEntity.success(index);
    }
}
