package com.wzw.point.client.plan.step.handler;

public interface StepHandler<T> {

    /**
     * 附加信息
     * @return T
     */
    T attachment();

    /**
     * 业务逻辑处理
     * @param attachment 附加信息，来自attachment方法的返回值
     * @return Object
     */
    Object doHandle(T attachment);

}
