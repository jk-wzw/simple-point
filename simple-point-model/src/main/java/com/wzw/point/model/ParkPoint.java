package com.wzw.point.model;

import java.io.Serializable;

/**
 * 正常的状态流转只有两种
 * null -> parking -> unpark -> null
 * null -> unpark -> null
 * @author xj-wzw
 */
public class ParkPoint implements Serializable {

    private static final long serialVersionUID = 8125629124149537643L;

    private final String name;

    private final ServiceAddress address;

    private ParkState prevState;

    private ParkState state;

    public enum ParkState {
        PARKING,
        UNPARK;
    }

    public ParkPoint(String name, ServiceAddress address) {
        this.name = name;
        this.address = address;
    }

    public void updateState(ParkState state) {
        this.prevState = state;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public ServiceAddress getAddress() {
        return address;
    }

    public ParkState getPrevState() {
        return prevState;
    }

    public ParkState getState() {
        return state;
    }

}
