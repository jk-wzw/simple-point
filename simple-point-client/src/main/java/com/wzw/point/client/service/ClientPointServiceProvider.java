package com.wzw.point.client.service;

import com.wzw.point.client.ClientConfigManager;
import com.wzw.point.client.service.impl.ClientPointServiceImpl;
import com.wzw.point.service.ClientPointService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

public class ClientPointServiceProvider {

    public void produce() {
        ClientPointService clientPointService = new ClientPointServiceImpl();

        // 应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(ClientConfigManager.getInstance().getApplicationName());

        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setHost(ClientConfigManager.getInstance().getServiceHost());
        protocolConfig.setPort(ClientConfigManager.getInstance().getServicePort());
        protocolConfig.setThreads(20);

        // 直接，不使用注册中心
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");

        // 服务提供者服务配置
        ServiceConfig<ClientPointService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(ClientPointService.class);
        serviceConfig.setRef(clientPointService);
        serviceConfig.setVersion("1.0.0");

        // 暴露及注册服务
        serviceConfig.export();
    }

}
