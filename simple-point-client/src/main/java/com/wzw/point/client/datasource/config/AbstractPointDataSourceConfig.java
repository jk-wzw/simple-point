package com.wzw.point.client.datasource.config;

import com.wzw.point.client.datasource.PointDataSource;

import java.util.Objects;

public abstract class AbstractPointDataSourceConfig {

    private final String host;

    private final int port;

    private final String user;

    private final String password;


    public AbstractPointDataSourceConfig(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * 初始化pointDataSource
     * @return AbstractPointDataSource
     * @throws Exception
     */
    public abstract PointDataSource init() throws Exception;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPointDataSourceConfig)) return false;
        AbstractPointDataSourceConfig that = (AbstractPointDataSourceConfig) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                Objects.equals(user, that.user) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, user, password);
    }

    @Override
    public String toString() {
        return "TestDataSourceConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
