package com.wzw.point.client.datasource;

import javax.sql.DataSource;

/**
 * db型point数据源
 * @author xj-wzw
 */
public class DBPointDataSource implements PointDataSource {

    private DataSource dataSource;


    public DBPointDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
