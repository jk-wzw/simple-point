package com.wzw.point.client.plan;

import com.wzw.point.client.plan.step.Step;

public class Plan {

    private final String name;

    private final Step step;

    public Plan(String name, Step step) {
        this.name = name;
        this.step = step;
    }

    public Step getStep() {
        return step;
    }

}
