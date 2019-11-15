package com.wzw.point.model;

import com.wzw.point.model.handler.PointHandler;

import java.io.Serializable;
import java.util.Map;

public class PointHandlerDTO implements Serializable {

    private static final long serialVersionUID = -739224021326320219L;

    /**
     * 标识连接的唯一id
     */
    private long id;

    /**
     * key -> pointName, value -> TestPointHandler
     */
    private Map<String, PointHandler> pointHandlers;


    public PointHandlerDTO(long id, Map<String, PointHandler> pointHandlers) {
        this.id = id;
        this.pointHandlers = pointHandlers;
    }

    public long getId() {
        return id;
    }

    public Map<String, PointHandler> getPointHandlers() {
        return pointHandlers;
    }

}
