package com.wzw.point.client.plan.step;

public abstract class Step {

    private Step nextStep;

    public enum StepType {
        // 自定义
        CUSTOM,
        // 执行sql
        SQL,
        // 放置point handler
        POINT,
        // 执行操作系统命令
        COMMAND,
        // 断言
        ASSERT;
    }

    public Step next(Step nextStep) {
        this.nextStep = nextStep;
        return this.nextStep;
    }

    public Step next() {
        return nextStep;
    }

    /**
     * step类型
     * @return StepType
     */
    public abstract StepType type();

    /**
     * 获取step执行结果
     * @return Object
     */
    public abstract Object getResult();

}
