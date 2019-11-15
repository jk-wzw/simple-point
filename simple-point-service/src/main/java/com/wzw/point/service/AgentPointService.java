package com.wzw.point.service;

import com.wzw.point.model.ParkPoint;
import com.wzw.point.model.PointHandlerDTO;

/**
 * agent端连接
 */
public interface AgentPointService {

    /**
     * 放置埋点处理器
     * @param pointHandlerDTO 数据传输对象
     * @return
     */
    boolean setPointHandler(PointHandlerDTO pointHandlerDTO);

    /**
     * 唤醒处于parking状态的连接
     * @param parkPoint
     */
    void unpark(ParkPoint parkPoint);

}
