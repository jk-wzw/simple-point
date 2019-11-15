package com.wzw.point.model.handler.impl;

import com.wzw.point.model.handler.PointHandler;

public class DemoPointHandler implements PointHandler {

    private static final long serialVersionUID = 3474467259338939750L;

    @Override
    public void doHandle(Object attachment) {
        System.err.println(">>>>> hello point <<<<<<");
    }

}
