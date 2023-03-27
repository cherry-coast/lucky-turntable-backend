package com.cherry.lucky.service.impl;

import com.cherry.lucky.common.exception.CherryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cherry.lucky.entity.InterfaceLog;
import com.cherry.lucky.mapper.InterfaceLogMapper;
import com.cherry.lucky.service.InterfaceLogService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName LuckyInterfaceLogServiceImpl
 * @author cherry
 * @version 1.0.0
 * @Description 
 * @createTime 2023年03月27日 10:42:00
 */
@Slf4j
@Service
public class InterfaceLogServiceImpl extends ServiceImpl<InterfaceLogMapper, InterfaceLog> implements InterfaceLogService {

    @Override
    public void saveLog(com.cherry.lucky.domain.InterfaceLog interfaceLog) {
        InterfaceLog luckyInterfaceLog = new InterfaceLog();
        BeanUtils.copyProperties(interfaceLog, luckyInterfaceLog);
        luckyInterfaceLog.setParameter(
                interfaceLog.getParameter() != null ? interfaceLog.getParameter().toString() : ""
        );
        luckyInterfaceLog.setResult(
                interfaceLog.getResult() != null ? interfaceLog.getResult().toString() : ""
        );
        int insertResult = this.baseMapper.insert(luckyInterfaceLog);
        if (insertResult <= 0) {
            log.info("save log error, interfaceLog : {} ", interfaceLog);
        }
    }

    public void saveLogAsJdbc(com.cherry.lucky.domain.InterfaceLog interfaceLog) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new CherryException(500, "MySqlDataBaseProcess failed to load drive !!! ");
        }

        String sql = "INSERT INTO lucky_interface_log " +
                "(DESCRIPTION, USERNAME, SPEND_TIME, BASE_PATH, URI, URL, `METHOD`, IP, `PARAMETER`, `RESULT` ) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        String jdbcUrl = "jdbc:mysql://124.221.143.138:3306/cherry-lucky";
        try(
                Connection connection = DriverManager.getConnection(jdbcUrl, "root", "root1234");
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, interfaceLog.getDescription());
            statement.setString(2, interfaceLog.getUsername());
            statement.setLong(3, interfaceLog.getSpendTime());
            statement.setString(4, interfaceLog.getBasePath());
            statement.setString(5, interfaceLog.getUri());
            statement.setString(6, interfaceLog.getUrl());
            statement.setString(7, interfaceLog.getMethod());
            statement.setString(8, interfaceLog.getIp());
            statement.setString(9, interfaceLog.getParameter() == null ? "" : interfaceLog.getParameter().toString());
            statement.setString(10, interfaceLog.getResult() == null ? "" : interfaceLog.getResult().toString());
            int insertResult = statement.executeUpdate();
            if (insertResult <= 0) {
                log.info("save log error, interfaceLog : {} ", interfaceLog);
            }
        } catch (SQLException sqlException) {
            log.info("save log error, interfaceLog : {} ", interfaceLog);
        }
    }
}
