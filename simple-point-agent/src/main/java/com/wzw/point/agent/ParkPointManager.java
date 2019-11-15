package com.wzw.point.agent;

import com.wzw.point.model.ParkPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParkPointManager {

    private static ParkPointManager instance = new ParkPointManager();

    private Map<String, ParkPoint> parkPointMap = new HashMap<>();


    private ParkPointManager() {

    }

    public static ParkPointManager getInstance() {
        return instance;
    }

    public ParkPoint getOrRegister(ParkPoint parkPoint) {
        ParkPoint lastParkPoint;
        if ((lastParkPoint = parkPointMap.get(parkPoint.getName())) == null) {
            parkPointMap.put(parkPoint.getName(), parkPoint);
            return parkPointMap.get(parkPoint.getName());
        }

        if (!Objects.equals(parkPoint.getAddress(), lastParkPoint.getAddress())) {
            throw new RuntimeException("The address is ambiguous, maybe you need check it.");
        }

        return lastParkPoint;
    }

    public ParkPoint get(String name) {
        return parkPointMap.get(name);
    }

}
