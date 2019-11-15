package com.wzw.point.client.plan.step;

import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * sql执行step
 * @author xj-wzw
 */
public class SqlStep extends Step {

    private final String sql;

    private final AbstractPointConnectionConfig connectionConfig;

    private FutureTask<Object> futureTask;

    private Object result;


    public SqlStep(String sql, AbstractPointConnectionConfig connectionConfig) {
        this.sql = sql;
        this.connectionConfig = connectionConfig;
    }

    @Override
    public StepType type() {
        return StepType.SQL;
    }

    /**
     * 阻塞获取sql执行结果
     * @return
     */
    @Override
    public Object getResult() {
        if (result != null) {
            return result;
        }
        try {
            return futureTask.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSql() {
        return sql;
    }

    public AbstractPointConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public void setFutureTask(FutureTask<Object> futureTask) {
        this.futureTask = futureTask;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
