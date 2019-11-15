package com.wzw.point.agent;

import com.wzw.point.model.PointTrace;
import com.wzw.point.model.handler.PointHandler;

import java.util.Objects;

public class Point {

    /**
     * 埋点名称，需要保证唯一
     */
    private final String name;

    /**
     * trace信息
     */
    private final PointTrace pointTrace;

    public Point(String name, PointTrace pointTrace) {
        this.name = name;
        this.pointTrace = pointTrace;
    }

    public void handle(long connectionId) {
        handle(connectionId, null);
    }

    public void handle(long connectionId, Object attachment) {
        PointHandler pointHandler =
                PointManager.getInstance().getPointHandler(name, connectionId);
        if (pointHandler == null) {
            return;
        }
        pointHandler.doHandle(attachment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Objects.equals(name, point.name) &&
                Objects.equals(pointTrace, point.pointTrace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pointTrace);
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", pointTrace=" + pointTrace +
                '}';
    }

}
