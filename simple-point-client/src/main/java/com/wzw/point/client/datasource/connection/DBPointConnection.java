package com.wzw.point.client.datasource.connection;

import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * db类型point连接
 * @author xj-wzw
 */
public class DBPointConnection extends AbstractPointConnection {

    private Connection connection;

    public DBPointConnection(AbstractPointConnectionConfig connectionConfig, Connection connection) {
        super(connectionConfig);
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        while (this.isBorrowed) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
        this.isBorrowed = true;
        return connection;
    }

    public Connection getConnection2() {
        return connection;
    }

    public void returnConnection() {
        this.isBorrowed = false;
    }

}
