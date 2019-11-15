package com.wzw.point.client.datasource.connection.config;

import com.wzw.point.client.datasource.DBPointDataSource;
import com.wzw.point.client.datasource.PointDataSource;
import com.wzw.point.client.datasource.PointDataSourceManager;
import com.wzw.point.client.datasource.config.AbstractPointDataSourceConfig;
import com.wzw.point.client.datasource.connection.AbstractPointConnection;
import com.wzw.point.client.datasource.connection.DBPointConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * db型point连接配置
 * @author xj-wzw
 */
public class DBPointConnectionConfig extends AbstractPointConnectionConfig{

    private boolean autocommit;

    public DBPointConnectionConfig(String name, AbstractPointDataSourceConfig dataSourceConfig) {
        super(name, dataSourceConfig);
    }

    @Override
    public DBPointConnection init() {
        Connection connection = null;
        Long connectionId = null;
        Statement statement = null;
        ResultSet rs = null;

        PointDataSource pointDataSource = PointDataSourceManager.getInstance().fetch(dataSourceConfig);
        DataSource dataSource = ((DBPointDataSource) pointDataSource).getDataSource();
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(this.autocommit);
            rs = statement.executeQuery("select connection_id()");
            rs.next();
            connectionId = rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        DBPointConnection dbPointConnection = new DBPointConnection(this, connection);
        dbPointConnection.setId(connectionId);
        return dbPointConnection;
    }

    public DBPointConnectionConfig(String name, AbstractPointDataSourceConfig dataSourceConfig, boolean autocommit) {
        super(name, dataSourceConfig);
        this.autocommit = autocommit;
    }

    public boolean isAutocommit() {
        return autocommit;
    }

}
