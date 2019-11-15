package com.wzw.point.client.datasource.config;

import com.wzw.point.client.datasource.PointDataSource;

/**
 * todo 待扩展
 * @author xj-wzw
 */
public class HttpPointDataSourceConfig extends AbstractPointDataSourceConfig {

    public HttpPointDataSourceConfig(String host, int port, String user, String password) {
        super(host, port, user, password);
    }

    @Override
    public PointDataSource init() {
        return null;
    }

}
