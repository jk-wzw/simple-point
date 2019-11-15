package com.wzw.point.client.datasource.connection;

import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * point连接管理器
 * @author xj-wzw
 */
public class PointConnectionManager {

    private static PointConnectionManager instance = new PointConnectionManager();

    private Map<AbstractPointConnectionConfig, AbstractPointConnection> connectionMap = new HashMap<>();

    private PointConnectionManager() {

    }

    public static PointConnectionManager getInstance() {
        return instance;
    }

    public AbstractPointConnection getConnection(AbstractPointConnectionConfig connectionConfig) {
        AbstractPointConnection pointConnection;
        if ((pointConnection = connectionMap.get(connectionConfig)) != null) {
            try {
                Connection connection =  ((DBPointConnection)pointConnection).getConnection2();
                if (!connection.isValid(1)) {
                    pointConnection = connectionConfig.init();
                    connectionMap.put(connectionConfig, pointConnection);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            pointConnection = connectionConfig.init();
            connectionMap.put(connectionConfig, pointConnection);
        }
        return connectionMap.get(connectionConfig);
    }

}
