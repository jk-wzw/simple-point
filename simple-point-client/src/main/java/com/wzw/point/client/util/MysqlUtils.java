package com.wzw.point.client.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.wzw.point.client.datasource.config.DBPointDataSourceConfig;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MysqlUtils {

    private static Properties properties = new Properties();

    static {
        properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        /**
         * 配置初始化大小、最大活跃、最小空闲
         */
        properties.setProperty("initialSize", "5");
        properties.setProperty("maxActive", "10");
        properties.setProperty("minIdle", "3");

        // 配置获取连接的等待超时时间
        properties.setProperty("maxWait", "3000");

        properties.setProperty("validationQuery", "select 1");
        properties.setProperty("testOnBorrow", "true");
    }

    public static DataSource initDataSource(DBPointDataSourceConfig dbConfig) throws Exception {
        String url = "jdbc:mysql://"
                + dbConfig.getAddress()
                + "/" + dbConfig.getDatabase()
                + "?useUnicode=true&characterEncoding=utf-8";

        properties.setProperty("url", url);
        properties.setProperty("username", dbConfig.getUser());
        properties.setProperty("password", dbConfig.getPassword());

        return DruidDataSourceFactory.createDataSource(properties);
    }

    public static Object parseResult(Statement statement) throws SQLException {
        if (statement.getUpdateCount() > 0) {
            return statement.getUpdateCount();
        } else {
            List<Map<String, Object>> result = new ArrayList<>();
            ResultSet rs = statement.getResultSet();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsMetaData.getColumnName(i + 1);
                    Object columnValue = rs.getObject(columnName);
                    record.put(columnName, columnValue);
                }
                result.add(record);
            }
            return result;
        }
    }

}
