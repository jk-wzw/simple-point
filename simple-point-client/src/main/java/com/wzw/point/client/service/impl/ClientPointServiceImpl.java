package com.wzw.point.client.service.impl;

import com.wzw.point.model.ParkPoint;
import com.wzw.point.service.ClientPointService;
import com.wzw.point.util.PointApiUtils;

public class ClientPointServiceImpl implements ClientPointService {

    @Override
    public void park(ParkPoint parkPoint) {
        PointApiUtils.park(parkPoint);
    }

    @Override
    public void unpark(ParkPoint parkPoint) {
        PointApiUtils.unpark(parkPoint);
    }

}
