package com.wzw.point.client.plan.step;

/**
 * @author xj-wzw
 */
public class AssertStep extends Step {

    private final Step target;

    private final Object expectedValue;

    private Boolean result;

    public AssertStep(Step target, Object expectedValue) {
        this.target = target;
        this.expectedValue = expectedValue;
    }

    @Override
    public StepType type() {
        return StepType.ASSERT;
    }

    @Override
    public Boolean getResult() {
        return result;
    }

    public Step target() {
        return target;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

}
