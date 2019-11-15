package com.wzw.point.service;

import com.wzw.point.model.ParkPoint;

/**
 * 客户端服务
 */
public interface ClientPointService {

    /**
     * park连接
     * @param parkPoint
     */
    void park(ParkPoint parkPoint);

    /**
     * unpark连接
     * @param parkPoint
     */
    void unpark(ParkPoint parkPoint);

}
