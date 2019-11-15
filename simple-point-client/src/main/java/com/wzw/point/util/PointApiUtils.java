package com.wzw.point.util;

import com.wzw.point.client.service.AgentPointServiceConsumer;
import com.wzw.point.model.ParkPoint;

public class PointApiUtils {

    public static void hello() {
        System.err.println(">>>>>> hello client <<<<<<");
    }

    public static void park(ParkPoint parkPoint) {
        throw new RuntimeException("only the agent can do this");
    }

    public static void unpark(ParkPoint parkPoint) {
        AgentPointServiceConsumer.getInstance().getPointService(parkPoint.getAddress()).unpark(parkPoint);
    }

}
