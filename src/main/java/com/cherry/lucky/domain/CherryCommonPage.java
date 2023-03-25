package com.cherry.lucky.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cherry.lucky.utils.CherryCollectionUtil;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author : ganxiongwen
 * Date: 2022/4/21 15:40
 * Description:
 * ClassName: CherryCommonPage
 * Package: com.cherry.flow.common.domain
 * Copyright (c) 2022,All Rights Reserved.
 */
@Data
@SuppressWarnings(value = "unused")
public class CherryCommonPage<T> {
 
    private long pageNo;
    private long pageSize;
    private long totalPage;
    private long total;
    private List<T> data;

    /**
     * 将mybatis原生分页后的list转为分页信息
     */
    public static <T> CherryCommonPage<T> restPage(IPage<T> page) {
        CherryCommonPage<T> cherryCommonPage = new CherryCommonPage<>();
        cherryCommonPage.setPageNo(page.getCurrent());
        cherryCommonPage.setPageSize(page.getSize());
        cherryCommonPage.setTotalPage(page.getTotal());
        cherryCommonPage.setTotal(page.getPages());
        cherryCommonPage.setData(page.getRecords());
        return cherryCommonPage;
    }

    /**
     * 将PageHelper插件分页后的list转为分页信息
     */
    public static <T> CherryCommonPage<T> restPage(List<T> list) {
        CherryCommonPage<T> cherryCommonPage = new CherryCommonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list, 20);
        cherryCommonPage.setTotalPage(pageInfo.getPages());
        cherryCommonPage.setPageNo(pageInfo.getPageNum());
        cherryCommonPage.setPageSize(pageInfo.getPageSize());
        cherryCommonPage.setTotal(pageInfo.getTotal());
        cherryCommonPage.setData(pageInfo.getList());
        return cherryCommonPage;
    }

    /**
     *
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CherryCommonPage<T> restPage(Page<T> pageInfo) {
        CherryCommonPage<T> cherryCommonPage = new CherryCommonPage<>();
        cherryCommonPage.setTotalPage(pageInfo.getTotalPages());
        cherryCommonPage.setPageNo(pageInfo.getNumber());
        cherryCommonPage.setPageSize(pageInfo.getSize());
        cherryCommonPage.setTotal(pageInfo.getTotalElements());
        cherryCommonPage.setData(pageInfo.getContent());
        return cherryCommonPage;
    }

    /**
     *
     * @param sourcePage Object before conversion
     * @param targetClazz Object after conversion
     * @return Converted page
     */
    public static <T> CherryCommonPage<T> transferPageData(CherryCommonPage<?> sourcePage, Class<T> targetClazz) {
        List<T> targetList = CherryCollectionUtil.copyList(sourcePage.getData(), targetClazz);
        CherryCommonPage<T> targetPage = CherryCommonPage.restPage(targetList);
        BeanUtils.copyProperties(sourcePage, targetPage, "data");
        return targetPage;
    }

}