package com.cherry.lucky.utils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cherry
 */
@Slf4j
@SuppressWarnings(value = "unused")
public class CherryCollectionUtil {

    /**
     *
     * 判断一个Collection是否为空， 包含List，Set，Queue
     * @param collection 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean listIsEmpty(Collection<?> collection) {
        return CollectionUtil.isEmpty(collection);
    }

    /**
     * 判断一个Collection是否非空，包含List，Set，Queue
     * @param collection 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean listIsNotEmpty(Collection<?> collection) {
        return !listIsEmpty(collection);
    }

    /**
     *
     * 判断一个Map是否为空
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean mapIsEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     *
     * 判断一个Map是否为空
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean mapIsNotEmpty(Map<?, ?> map) {
        return !mapIsEmpty(map);
    }

    /**
     *
     * 判断一个Array是否为空
     * @param objects 要判断的Object[]
     * @return true：空 false：非空
     */
    public static boolean arrayIsEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 单个类转换
     *
     * @param sourceObj source object
     * @param targetClass target object
     * @return target object
     */
    public static <T> T convert(Object sourceObj, Class<T> targetClass) {
        if (sourceObj == null || targetClass == null) {
            return null;
        }
        T targetObj;
        try {
            targetObj = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("An error occurred while copying and convert the list detail : {}", e.toString());
            return null;
        }
        BeanUtils.copyProperties(sourceObj, targetObj);
        return targetObj;
    }

    /**
     * copy List
     *
     * @param sourceList source list
     * @param targetClass target class
     * @return target class list
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (CherryCollectionUtil.listIsEmpty(sourceList) || targetClass == null) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(sourceObj -> convert(sourceObj, targetClass)).collect(Collectors.toList());
    }

}
