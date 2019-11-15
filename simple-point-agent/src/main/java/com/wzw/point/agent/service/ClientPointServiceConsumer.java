package com.wzw.point.agent.service;

import com.wzw.point.agent.AgentConfigManager;
import com.wzw.point.service.AgentPointService;
import com.wzw.point.service.ClientPointService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.context.ConfigManager;

/**
 * client服务消费者
 * @author xj-wzw
 */
public class ClientPointServiceConsumer {

    private static final String SERVICE_NAME = "com.wzw.point.service.ClientPointService";

    private static ClientPointServiceConsumer instance = new ClientPointServiceConsumer();

    private static ClientPointService clientPointService = loadPointService();

    public static ClientPointServiceConsumer getInstance() {
        return instance;
    }

    private ClientPointServiceConsumer() {

    }

    private static ClientPointService loadPointService() {
        // 应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(AgentConfigManager.getInstance().getApplicationName());

        if (ConfigManager.getInstance().getApplication().isPresent()) {
            applicationConfig = ConfigManager.getInstance().getApplication().get();
        }

        // 直连
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");

        // 引用远程服务
        ReferenceConfig<ClientPointService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(AgentPointService.class);
        referenceConfig.setUrl("dubbo://" + AgentConfigManager.getInstance().getClientAddress() + "/" + SERVICE_NAME);
        referenceConfig.setVersion("1.0.0");

        // 此代理对象内部封装了所有通讯细节，对象较重，需要缓存使用
        return referenceConfig.get();
    }

    public ClientPointService getPointService() {
        return clientPointService;
    }

}
