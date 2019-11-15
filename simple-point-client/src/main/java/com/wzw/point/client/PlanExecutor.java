package com.wzw.point.client;

import com.wzw.point.client.datasource.connection.AbstractPointConnection;
import com.wzw.point.client.datasource.connection.DBPointConnection;
import com.wzw.point.client.datasource.connection.PointConnectionManager;
import com.wzw.point.client.datasource.connection.config.AbstractPointConnectionConfig;
import com.wzw.point.client.plan.Plan;
import com.wzw.point.client.plan.step.*;
import com.wzw.point.client.plan.step.handler.StepHandler;
import com.wzw.point.client.service.AgentPointServiceConsumer;
import com.wzw.point.client.util.MysqlUtils;
import com.wzw.point.model.PointHandlerDTO;
import com.wzw.point.model.handler.PointHandler;
import com.wzw.point.util.PointCmdUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * plan执行器
 *
 * @author xj-wzw
 */
public class PlanExecutor {

    private static final Long PLAN_FINISHED_CHECK_INTERVAL = 10L;

    private static PlanExecutor instance = new PlanExecutor();

    public static PlanExecutor getInstance() {
        return instance;
    }

    private PlanExecutor() {

    }

    private ExecuteMode executeMode = ExecuteMode.SERIAL;

    /**
     * 两种执行模式：1.串行 2.并发
     */
    public enum ExecuteMode {
        // 串行
        SERIAL,
        // 并发
        CONCURRENCY;
    }

    /**
     * 并发模式下 + agent访问执行 使用的线程池
     * 默认 操作系统核数+1
     * 可以自定义设置
     */
    private ExecutorService planExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    /**
     * 执行
     *
     * @param planList
     */
    public void execute(List<Plan> planList) {
        switch (executeMode) {
            case SERIAL:
                for (Plan plan : planList) {
                    executeOne(plan);
                }
                break;
            case CONCURRENCY:
                CountDownLatch countDownLatch = new CountDownLatch(planList.size());
                for (Plan plan : planList) {
                    FutureTask<Object> futureTask = new FutureTask<>(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            countDownLatch.countDown();
                            executeOne(plan);
                            return true;
                        }
                    });
                    planExecutorService.submit(futureTask);
                }
                break;
            default:
                break;
        }
        // 检查任务是否结束
        if (!planExecutorService.isShutdown()) {
            planExecutorService.shutdownNow();
            while (true) {
                if (planExecutorService.isTerminated()) {
                    System.err.println(">>>>>> plan执行完成");
                    break;
                }
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(PLAN_FINISHED_CHECK_INTERVAL));
            }
        }
    }

    public void executeOne(Plan plan) {
        Step step = plan.getStep();
        while (step != null) {
            executeOneStep(step);
            step = step.next();
        }
    }

    public void executeOneStep(Step step) {
        if (step == null || step.type() == null) {
            return;
        }
        switch (step.type()) {
            case POINT:
                PointStep pointStep = (PointStep) step;
                AbstractPointConnectionConfig connectionConfig = pointStep.getConnectionConfig();

                Map<String, PointHandler> pointHandlers = new HashMap<>();
                pointHandlers.put(pointStep.getPointName(), pointStep.getPointHandler());

                AbstractPointConnection pointConnection = PointConnectionManager.getInstance().getConnection(connectionConfig);
                PointHandlerDTO pointHandlerDTO = new PointHandlerDTO(pointConnection.getId(), pointHandlers);
                boolean result = AgentPointServiceConsumer.getInstance().getPointService((pointStep.getServiceAddress())).setPointHandler(pointHandlerDTO);
                pointStep.setResult(result);
                break;
            case SQL:
                SqlStep sqlStep = (SqlStep) step;
                AbstractPointConnectionConfig pointConnectionConfig = sqlStep.getConnectionConfig();
                DBPointConnection dbConnection = (DBPointConnection) PointConnectionManager.getInstance().getConnection(pointConnectionConfig);

                Connection connection = dbConnection.getConnection();
                FutureTask<Object> sqlTask = new FutureTask<>(new Callable<Object>() {
                    @Override
                    public Object call() {
                        Object sqlExecResult = null;
                        try (Statement statement = connection.createStatement()) {
                            statement.execute(sqlStep.getSql());
                            sqlExecResult = MysqlUtils.parseResult(statement);
                            sqlStep.setResult(sqlExecResult);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            dbConnection.returnConnection();
                        }
                        return sqlExecResult;
                    }
                });
                sqlStep.setFutureTask(sqlTask);

                planExecutorService.submit(sqlTask);
                break;
            case CUSTOM:
                CustomStep customStep = (CustomStep) step;
                StepHandler stepHandler = customStep.getStepHandler();
                Object customExecResult = stepHandler.doHandle(stepHandler.attachment());
                customStep.setResult(customExecResult);
                break;
            case COMMAND:
                CmdStep cmdStep = (CmdStep) step;
                List<String> cmdExecResult = PointCmdUtils.exec(cmdStep.getCommand());
                cmdStep.setResult(cmdExecResult);
                break;
            case ASSERT:
                AssertStep assertStep = (AssertStep) step;
                Step targetStep = assertStep.target();
                // 如果发现还没有结果，则先去执行
                if (targetStep.getResult() == null) {
                    executeOneStep(targetStep);
                }
                if (!Objects.equals(targetStep.getResult(), assertStep.getExpectedValue())) {
                    String msg = "Assert failed! actual result is: " + String.valueOf(targetStep.getResult())
                            + ", but expected result is: " + String.valueOf(assertStep.getExpectedValue());
                    throw new RuntimeException(msg);
                }
                assertStep.setResult(true);
                break;
            default:
                break;
        }
    }

}
