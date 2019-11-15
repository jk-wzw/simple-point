package com.wzw.point.util;

import com.wzw.point.agent.ParkPointManager;
import com.wzw.point.model.ParkPoint;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xj-wzw
 */
public class PointApiUtils {

    /**
     * 10ms
     */
    private static final Long DEFAULT_CHECK_INTERVAL = 10L;

    public static void hello() {
        System.err.println(">>>>>> hello agent <<<<<<");
    }

    public static void park(ParkPoint parkPoint) {
        ParkPointManager parkPointManager = ParkPointManager.getInstance();
        ParkPoint lastParkPoint = parkPointManager.getOrRegister(parkPoint);

        // 处理状态流转 null -> unpark -> null
        if (lastParkPoint.getPrevState() == null
                && lastParkPoint.getState() == ParkPoint.ParkState.UNPARK) {
            lastParkPoint.updateState(null);
            return;
        }

        // 处理状态流转 null -> parking
        if (lastParkPoint.getState() != null) {
            throw new IllegalStateException("Unexpected park state: " + lastParkPoint.getState().name());
        }
        lastParkPoint.updateState(ParkPoint.ParkState.PARKING);

        // 处理状态流转 parking |-> unpark -> null
        while (true) {
            if (lastParkPoint.getState() == ParkPoint.ParkState.UNPARK) {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(DEFAULT_CHECK_INTERVAL));
        }
        lastParkPoint.updateState(null);
    }

}
