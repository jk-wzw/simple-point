package com.wzw.point.model.handler;

import java.io.Serializable;

public interface PointHandler extends Serializable {

    void doHandle(Object attachment);

}
