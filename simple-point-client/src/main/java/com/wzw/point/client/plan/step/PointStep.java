package com.wzw.point.client.plan.step;

import com.wzw.point.client.datasource.connection.AbstractPointConnection;
import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;
import com.wzw.point.model.ServiceAddress;
import com.wzw.point.model.handler.PointHandler;

public class PointStep extends Step {

    private final String pointName;

    private final PointHandler pointHandler;

    private final AbstractPointConnectionConfig connectionConfig;

    private final ServiceAddress serviceAddress;

    private Boolean result;

    public PointStep(String pointName, PointHandler pointHandler, AbstractPointConnectionConfig connectionConfig, ServiceAddress serviceAddress) {
        this.pointName = pointName;
        this.pointHandler = pointHandler;
        this.connectionConfig = connectionConfig;
        this.serviceAddress = serviceAddress;
    }

    @Override
    public StepType type() {
        return StepType.POINT;
    }

    @Override
    public Boolean getResult() {
        return result;
    }

    public String getPointName() {
        return pointName;
    }

    public PointHandler getPointHandler() {
        return pointHandler;
    }

    public AbstractPointConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

}
