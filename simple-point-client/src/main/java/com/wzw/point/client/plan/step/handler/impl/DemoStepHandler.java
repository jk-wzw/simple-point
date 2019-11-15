package com.wzw.point.client.plan.step.handler.impl;

import com.wzw.point.client.plan.step.handler.StepHandler;

public class DemoStepHandler implements StepHandler<String> {

    @Override
    public String attachment() {
        return "hello, custom step handler";
    }

    @Override
    public Object doHandle(String attachment) {
        System.err.println(">>>>>> " + attachment + "<<<<<<");
        return attachment;
    }

}
