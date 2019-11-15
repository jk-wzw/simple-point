package com.wzw.point.model;

import java.io.Serializable;
import java.util.Objects;

public class ServiceAddress implements Serializable {

    private static final long serialVersionUID = 8632248873237415501L;

    private final String ip;

    private final int port;

    public ServiceAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getAddress() {
        return ip + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceAddress that = (ServiceAddress) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

}
