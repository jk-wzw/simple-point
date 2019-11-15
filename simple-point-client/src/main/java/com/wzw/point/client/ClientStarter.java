package com.wzw.point.client;

import com.wzw.point.client.datasource.DBPointDataSource;
import com.wzw.point.client.datasource.config.DBPointDataSourceConfig;
import com.wzw.point.client.service.ClientPointServiceProvider;
import com.wzw.point.model.ServiceAddress;
import com.wzw.point.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClientStarter {

    private static ClientStarter instance = new ClientStarter();

    private static final String POINT_CONFIG_FILE_PATH = "/point.properties";

    private static final String SERVICE = "service";
    private static final String SERVICE_PORT_ATTR = "port";

    private static final String AGENT = "agent";
    private static final String AGENT_HOST_ATTR = "host";
    private static final String AGENT_PORT_ATTR = "port";

    private static final String DB_TYPE = "db";

    private static final String TYPE_ATTR = "type";
    private static final String HOST_ATTR = "host";
    private static final String PORT_ATTR = "port";
    private static final String USER_ATTR = "user";
    private static final String PASSWORD_ATTR = "password";
    private static final String DATABASE_ATTR = "database";

    private volatile boolean isRunning = false;

    private ClientStarter() {

    }

    public static ClientStarter getInstance() {
        return instance;
    }

    public void startup() {
        if (isRunning) {
            throw new RuntimeException("Client is already running.");
        }
        isRunning = false;

        loadConfig();

        ClientPointServiceProvider provider = new ClientPointServiceProvider();
        provider.produce();
    }

    private void loadConfig() {
        Properties properties = loadConfigProperties();

        // 临时存放解析的数据源配置信息
        Map<String, Map<String, String>> dataSourceConfigs = new HashMap<>();
        // 临时存放agent服务地址的配置信息
        Map<String, Map<String, String>> agentAddressConfigs = new HashMap<>();

        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propName = String.valueOf(enumeration.nextElement());
            String propValue = properties.getProperty(propName);

            // 属性值非空拦截
            if (StringUtils.isEmpty(propValue)) {
                throw new RuntimeException("Property[" + propName + "] value is empty.");
            }

            String lowerPropName = propName.toLowerCase();

            String errMsg = "Property[" + propName + "] name is illegal.";

            // 属性名称合法性检查
            if (!StringUtils.contains(propName, ".")) {
                throw new RuntimeException(errMsg);
            }

            String[] attrs = StringUtils.split(lowerPropName, ".");

            // 空属性检查
            checkEmptyAttr(attrs, errMsg);

            if (attrs.length == 2) {
                if (StringUtils.equalsIgnoreCase(attrs[0], SERVICE)) {
                    if (SERVICE_PORT_ATTR.equalsIgnoreCase(attrs[1])) {
                        if (ClientConfigManager.getInstance().getServicePort() != -1) {
                            throw new RuntimeException("Config 'service.port' already exists.");
                        }
                        ClientConfigManager.getInstance().setServicePort(Integer.parseInt(propValue));
                    } else {
                        throw new RuntimeException(errMsg);
                    }
                } else if (checkDataSourceAttrIsValid(attrs[1])) {
                    String dataSourceName = attrs[0];
                    dataSourceConfigs.putIfAbsent(dataSourceName, new HashMap<>());
                    Map<String, String> dataSourceConfig = dataSourceConfigs.get(dataSourceName);

                    String dataSourceAttr = attrs[1];
                    checkAttrIsDuplicated(dataSourceConfig, dataSourceAttr, propName);

                    dataSourceConfig.put(dataSourceAttr, propValue);
                } else {
                    throw new RuntimeException("Unrecognized attribute '" + attrs[1] + "of property '" + propName + "'.");
                }
            } else if (attrs.length == 3 && StringUtils.equalsIgnoreCase(attrs[0], AGENT)) {
                String agentName = attrs[1];
                agentAddressConfigs.putIfAbsent(agentName, new HashMap<>());
                Map<String, String> agentAddressConfig = agentAddressConfigs.get(agentName);

                String agentServiceAttr = attrs[2];
                checkAttrIsDuplicated(agentAddressConfig, agentServiceAttr, propName);

                agentAddressConfig.put(agentServiceAttr, propValue);
            } else {
                throw new RuntimeException(errMsg);
            }
        }

        if (ClientConfigManager.getInstance().getServicePort() == -1) {
            throw new RuntimeException("No 'service.port' configured.");
        }

        for (Map.Entry<String, Map<String, String>> agentServiceConfig : agentAddressConfigs.entrySet()) {
            String agentName = agentServiceConfig.getKey();
            Map<String, String> agentAddressConfigMap = agentServiceConfig.getValue();

            String agentAddressHost = checkAgentAddressAttrIsNotConfigured(agentAddressConfigMap, AGENT_HOST_ATTR, agentName);
            String agentAddressPort = checkAgentAddressAttrIsNotConfigured(agentAddressConfigMap, AGENT_PORT_ATTR, agentName);
            ServiceAddress serviceAddress = new ServiceAddress(agentAddressHost, Integer.parseInt(agentAddressPort));
            ClientConfigManager.getInstance().putAgentServiceAddress(agentName, serviceAddress);
        }

        for (Map.Entry<String, Map<String, String>> dataSourceConfig : dataSourceConfigs.entrySet()) {
            String dataSourceName = dataSourceConfig.getKey();
            Map<String, String> dataSourceConfigMap = dataSourceConfig.getValue();

            String dataSourceType = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, TYPE_ATTR, dataSourceName);
            if (dataSourceType.equalsIgnoreCase(DB_TYPE)) {
                String dataSourceHost = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, HOST_ATTR, dataSourceName);
                String dataSourcePort = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, PORT_ATTR, dataSourceName);
                String dataSourceUser = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, USER_ATTR, dataSourceName);
                String dataSourcePassword = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, PASSWORD_ATTR, dataSourceName);
                String dataSourceDatabase = checkDataSourceAttrIsNotConfigured(dataSourceConfigMap, DATABASE_ATTR, dataSourceName);
                DBPointDataSourceConfig dbDataSourceConfig
                        = new DBPointDataSourceConfig(dataSourceHost, Integer.parseInt(dataSourcePort), dataSourceUser, dataSourcePassword, dataSourceDatabase);
                ClientConfigManager.getInstance().putDataSourceConfig(dataSourceName, dbDataSourceConfig);
            }
        }
    }

    private void checkEmptyAttr(String[] attrs, String errMsg) {
        if (attrs.length > 0) {
            for (String attr : attrs) {
                if (StringUtils.isEmpty(attr)) {
                    throw new RuntimeException(errMsg);
                }
            }
        }
    }

    private boolean checkDataSourceAttrIsValid(String attrName) {
        return attrName.equals(TYPE_ATTR)
                || attrName.equals(HOST_ATTR)
                || attrName.equals(PORT_ATTR)
                || attrName.equals(USER_ATTR)
                || attrName.equals(PASSWORD_ATTR)
                || attrName.equals(DATABASE_ATTR);
    }

    private void checkAttrIsDuplicated(Map<String, String> configMap, String attrName, String propName) {
        if (configMap.get(attrName) != null) {
            throw new RuntimeException("'" + propName + "' config already exists.");
        }
    }

    private String checkDataSourceAttrIsNotConfigured(Map<String, String> configMap, String attrName, String dataSourceName) {
        if (configMap.get(attrName) == null) {
            String msg = "No '" + attrName + "' attribute configured for datasource[" + dataSourceName + "]";
            throw new RuntimeException(msg);
        }
        return configMap.get(attrName);
    }

    private String checkAgentAddressAttrIsNotConfigured(Map<String, String> configMap, String attrName, String agentName) {
        if (configMap.get(attrName) == null) {
            String msg = "No '" + attrName + "' attribute configured for agent[" + agentName + "]";
            throw new RuntimeException(msg);
        }
        return configMap.get(attrName);
    }

    private Properties loadConfigProperties() {
        InputStream in = this.getClass().getResourceAsStream(POINT_CONFIG_FILE_PATH);

        Properties properties = new Properties();
        InputStreamReader isr = null;

        try {
            isr = new InputStreamReader(in, "utf-8");
            properties.load(isr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }

}
