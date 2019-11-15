package com.wzw.point.model;

import java.util.Objects;

public class PointTrace {

    public final String className;

    public final String methodName;

    public final int lineNum;

    public PointTrace(String className, String methodName, int lineNum) {
        this.className = className;
        this.methodName = methodName;
        this.lineNum = lineNum;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getLineNum() {
        return lineNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointTrace pointTrace = (PointTrace) o;
        return lineNum == pointTrace.lineNum &&
                Objects.equals(className, pointTrace.className) &&
                Objects.equals(methodName, pointTrace.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, lineNum);
    }

    @Override
    public String toString() {
        return "TraceInfo{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNum=" + lineNum +
                '}';
    }

}
