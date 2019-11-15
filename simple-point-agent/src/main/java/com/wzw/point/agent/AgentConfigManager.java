package com.wzw.point.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * point配置管理器
 * @author xj-wzw
 */
public class AgentConfigManager {

    private String applicationName = "simple-point-agent";

    private String serviceHost;

    private volatile int servicePort = -1;

    private String clientHost;

    private volatile int clientPort = -1;

    private static AgentConfigManager instance = new AgentConfigManager();

    public static AgentConfigManager getInstance() {
        return instance;
    }

    private AgentConfigManager() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            serviceHost = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getClientAddress() {
        return this.clientHost + ":" + this.clientPort;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

}
