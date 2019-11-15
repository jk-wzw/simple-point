package com.wzw.point.client.plan.step;

import java.util.List;

public class CmdStep extends Step {

    private final String command;

    private List<String> result;


    public CmdStep(String command) {
        this.command = command;
    }

    @Override
    public StepType type() {
        return StepType.COMMAND;
    }

    @Override
    public List<String> getResult() {
        return result;
    }

    public String getCommand() {
        return command;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

}
