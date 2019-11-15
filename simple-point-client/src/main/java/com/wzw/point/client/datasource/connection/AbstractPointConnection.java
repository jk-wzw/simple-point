package com.wzw.point.client.datasource.connection;

import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;

public abstract class AbstractPointConnection {

    private Long id;

    private AbstractPointConnectionConfig connectionConfig;

    protected volatile boolean isBorrowed = false;


    public AbstractPointConnection(AbstractPointConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractPointConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

}
