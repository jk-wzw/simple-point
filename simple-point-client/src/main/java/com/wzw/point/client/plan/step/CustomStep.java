package com.wzw.point.client.plan.step;

import com.wzw.point.client.plan.step.handler.StepHandler;

public class CustomStep extends Step {

    private final StepHandler stepHandler;

    private Object result;


    public CustomStep(StepHandler stepHandler) {
        this.stepHandler = stepHandler;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public StepHandler getStepHandler() {
        return stepHandler;
    }

    @Override
    public StepType type() {
        return StepType.CUSTOM;
    }

    @Override
    public Object getResult() {
        return result;
    }

}
