package com.wzw.point.client.datasource.connection.config;

import com.wzw.point.client.datasource.config.AbstractPointDataSourceConfig;
import com.wzw.point.client.datasource.connection.AbstractPointConnection;

import java.util.Objects;

public abstract class AbstractPointConnectionConfig {

    private final String name;

    protected final AbstractPointDataSourceConfig dataSourceConfig;


    public AbstractPointConnectionConfig(String name, AbstractPointDataSourceConfig dataSourceConfig) {
        this.name = name;
        this.dataSourceConfig = dataSourceConfig;
    }

    /**
     * 初始化连接
     * @return AbstractPointConnection
     */
    public abstract AbstractPointConnection init();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPointConnectionConfig that = (AbstractPointConnectionConfig) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(dataSourceConfig, that.dataSourceConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataSourceConfig);
    }

}
