package com.wzw.point.agent.service;

import com.wzw.point.agent.AgentConfigManager;
import com.wzw.point.agent.service.impl.AgentPointServiceImpl;
import com.wzw.point.service.AgentPointService;
import com.wzw.point.service.ClientPointService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

/**
 * agent服务提供者
 * @author xj-wzw
 */
public class AgentPointServiceProvider {

    public void produce() {
        AgentPointService clientPointService = new AgentPointServiceImpl();

        // 应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(AgentConfigManager.getInstance().getApplicationName());

        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setHost(AgentConfigManager.getInstance().getServiceHost());
        protocolConfig.setPort(AgentConfigManager.getInstance().getServicePort());
        protocolConfig.setThreads(20);

        // 直接，不使用注册中心
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");

        // 服务提供者服务配置
        ServiceConfig<AgentPointService> serviceConfig = new ServiceConfig<>();
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
