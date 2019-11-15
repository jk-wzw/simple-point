package com.wzw.point.agent.service.impl;

import com.wzw.point.agent.ParkPointManager;
import com.wzw.point.agent.PointManager;
import com.wzw.point.model.ParkPoint;
import com.wzw.point.model.PointHandlerDTO;
import com.wzw.point.model.handler.PointHandler;
import com.wzw.point.service.AgentPointService;

import java.util.Map;

public class AgentPointServiceImpl implements AgentPointService {

    @Override
    public boolean setPointHandler(PointHandlerDTO pointHandlerDTO) {
        PointManager pointManager = PointManager.getInstance();
        Map<String, PointHandler> pointHandlers = pointHandlerDTO.getPointHandlers();

        for (Map.Entry<String, PointHandler> pointHandlerEntry : pointHandlers.entrySet()) {
            String pointName = pointHandlerEntry.getKey();
            pointManager.setPointHandler(pointName, pointHandlerDTO.getId(), pointHandlerEntry.getValue());
        }
        return true;
    }

    @Override
    public void unpark(ParkPoint parkPoint) {
        ParkPoint lastParkPoint = ParkPointManager.getInstance().getOrRegister(parkPoint);
        /**
         * 处理状态流转:
         * null -> unpark
         * parking -> unpark
         */
        if (lastParkPoint.getState() == null
                || lastParkPoint.getState() == ParkPoint.ParkState.PARKING) {
            lastParkPoint.updateState(ParkPoint.ParkState.UNPARK);
        } else {
            throw new IllegalStateException("Unexpected park state: " + lastParkPoint.getState().name());
        }
    }

}
