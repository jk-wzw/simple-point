package com.wzw.point.agent.util;

import com.wzw.point.model.PointTrace;

/**
 * 收集trace信息
 * @author xj-wzw
 */
public class TraceUtil {

    public static PointTrace getPointTrace() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return new PointTrace(stacks[2].getClassName(), stacks[2].getMethodName(),stacks[2].getLineNumber());
    }

}
