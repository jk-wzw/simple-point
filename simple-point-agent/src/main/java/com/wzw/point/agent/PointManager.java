package com.wzw.point.agent;

import com.wzw.point.agent.util.TraceUtil;
import com.wzw.point.model.PointTrace;
import com.wzw.point.model.handler.PointHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * point管理器
 *
 * @author xj-wzw
 */
public class PointManager {

    private static PointManager instance = new PointManager();

    private Map<String, Point> points = new ConcurrentHashMap<>();

    /**
     * 将handler和point分离，避免client端set handler时找不到对应name的point
     * pointName -> (connectionId -> pointHandler)
     */
    private Map<String, Map<Long, PointHandler>> pointHandlers = new HashMap<>();

    private PointManager() {

    }

    public static PointManager getInstance() {
        return instance;
    }

    public Point mark(String pointName) {
        Point point;
        if ((point = points.get(pointName)) != null) {
            return point;
        }
        point = new Point(pointName, TraceUtil.getPointTrace());
        points.put(pointName, point);
        return point;
    }

    public Point mark(String pointName, PointTrace pointTrace) {
        Point point;
        if ((point = points.get(pointName)) != null) {
            return point;
        }
        point = new Point(pointName, pointTrace);
        points.put(pointName, point);
        return point;
    }

    public Point getPoint(String name) {
        return points.get(name);
    }

    public void setPointHandler(String pointName, long connectionId, PointHandler pointHandler) {
        pointHandlers.putIfAbsent(pointName, new HashMap<>(16));
        pointHandlers.get(pointName).put(connectionId, pointHandler);
    }

    public PointHandler getPointHandler(String pointName, long connectionId) {
        Map<Long, PointHandler> pointHandlerMap;
        if ((pointHandlerMap = pointHandlers.get(pointName)) != null) {
            return pointHandlerMap.get(connectionId);
        }
        return null;
    }

}
