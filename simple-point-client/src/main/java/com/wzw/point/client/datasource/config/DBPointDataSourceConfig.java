package com.wzw.point.client.datasource.config;

import com.wzw.point.client.datasource.DBPointDataSource;
import com.wzw.point.client.util.MysqlUtils;

import java.util.Objects;

/**
 * db型point连接数据源配置
 * @author xj-wzw
 */
public class DBPointDataSourceConfig extends AbstractPointDataSourceConfig {

    private String database;


    public DBPointDataSourceConfig(String host, int port, String user, String password, String database) {
        super(host, port, user, password);
        this.database = database;
    }

    @Override
    public DBPointDataSource init() throws Exception {
        return new DBPointDataSource(MysqlUtils.initDataSource(this));
    }

    public String getDatabase() {
        return database;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBPointDataSourceConfig)) return false;
        if (!super.equals(o)) return false;
        DBPointDataSourceConfig that = (DBPointDataSourceConfig) o;
        return Objects.equals(database, that.database);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), database);
    }

}
