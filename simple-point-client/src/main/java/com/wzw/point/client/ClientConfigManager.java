package com.wzw.point.client;

import com.wzw.point.client.datasource.config.AbstractPointDataSourceConfig;
import com.wzw.point.model.ServiceAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 */
public class ClientConfigManager {

    private static ClientConfigManager instance = new ClientConfigManager();

    private String applicationName = "simple-point-client";

    private final String serviceHost;

    private volatile int servicePort = -1;

    private String agentHost;

    private volatile int agentPort = -1;

    private Map<String, AbstractPointDataSourceConfig> pointDataSourceConfigMap = new HashMap<>();

    private Map<String, ServiceAddress> agentServiceAddressMap = new HashMap<>();


    private ClientConfigManager() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            serviceHost = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClientConfigManager getInstance() {
        return instance;
    }

    public AbstractPointDataSourceConfig getDataSourceConfig(String name) {
        return pointDataSourceConfigMap.get(name);
    }

    public void putDataSourceConfig(String name, AbstractPointDataSourceConfig dataSourceConfig) {
        pointDataSourceConfigMap.put(name, dataSourceConfig);
    }

    public ServiceAddress getAgentServiceAddress(String name) {
        return agentServiceAddressMap.get(name);
    }

    public void putAgentServiceAddress(String name, ServiceAddress serviceAddress) {
        agentServiceAddressMap.put(name, serviceAddress);
    }

    public String getApplicationName() {
        return applicationName;
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

}
