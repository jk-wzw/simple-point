package com.wzw.point.agent.interceptor;

/**
 * 拦截器
 */
public interface PointInterceptor {

    /**
     * pre
     */
    void preHandle();

    /**
     * post
     */
    void postHandle();

}

