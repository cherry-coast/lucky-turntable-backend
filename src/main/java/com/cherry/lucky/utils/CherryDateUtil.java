package com.cherry.lucky.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName CherryDateUtil
 * @Description
 * @createTime 2023年05月19日 16:02:00
 */
@SuppressWarnings("unused")
public class CherryDateUtil {

    public static final String YYYY_DD_MM_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String HH_MM_SS = "HH:mm:ss";


    /**
     * 根据指定格式获取当前日期
     *
     * @param formatExample 格式示例
     * @return 当前日期
     */
    public static String getNowDate(String formatExample) {
        SimpleDateFormat format = new SimpleDateFormat(formatExample);
        return format.format(new Date(System.currentTimeMillis()));
    }

}
