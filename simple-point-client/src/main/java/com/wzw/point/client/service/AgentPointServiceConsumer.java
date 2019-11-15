package com.wzw.point.client.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wzw.point.client.ClientConfigManager;
import com.wzw.point.model.ServiceAddress;
import com.wzw.point.service.AgentPointService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.context.ConfigManager;

import java.util.concurrent.ExecutionException;

/**
 * agent服务消费者
 * @author xj-wzw
 */
public class AgentPointServiceConsumer {

    private static final String SERVICE_NAME = "com.wzw.point.service.ClientPointService";

    private static AgentPointServiceConsumer instance = new AgentPointServiceConsumer();

    private LoadingCache<ServiceAddress, AgentPointService> pointServices = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<ServiceAddress, AgentPointService>() {
        @Override
        public AgentPointService load(ServiceAddress serviceAddress) throws Exception {
            return loadPointService(serviceAddress);
        }
    });

    public static AgentPointServiceConsumer getInstance() {
        return instance;
    }

    private AgentPointServiceConsumer() {

    }

    private AgentPointService loadPointService(ServiceAddress serviceAddress) {
        // 应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(ClientConfigManager.getInstance().getApplicationName());

        if (ConfigManager.getInstance().getApplication().isPresent()) {
            applicationConfig = ConfigManager.getInstance().getApplication().get();
        }

        // 直连
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");

        // 引用远程服务
        ReferenceConfig<AgentPointService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(AgentPointService.class);
        referenceConfig.setUrl("dubbo://" + serviceAddress.getAddress() + "/" + SERVICE_NAME);
        referenceConfig.setVersion("1.0.0");

        // 此代理对象内部封装了所有通讯细节，对象较重，需要缓存使用
        return referenceConfig.get();
    }

    public AgentPointService getPointService(ServiceAddress serviceAddress) {
        AgentPointService agentPointService = null;
        try {
            agentPointService = pointServices.get(serviceAddress);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return agentPointService;
    }

}
