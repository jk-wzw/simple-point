package com.wzw.point.agent;

import com.wzw.point.agent.service.AgentPointServiceProvider;
import com.wzw.point.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author xj-wzw
 */
public class AgentStarter {

    private static final String POINT_CONFIG_FILE_PATH = "/point.properties";

    private static final String SERVICE = "service";
    private static final String SERVICE_PORT_ATTR = "port";

    private static final String CLIENT = "client";
    private static final String CLIENT_HOST_ATTR = "host";
    private static final String CLIENT_PORT_ATTR = "port";

    private volatile boolean isRunning = false;

    private static AgentStarter instance = new AgentStarter();

    private AgentStarter() {

    }

    public static AgentStarter getInstance() {
        return instance;
    }

    public void startup() {
        if (isRunning) {
            throw new RuntimeException("Agent is already running.");
        }
        isRunning = false;

        loadConfig();

        AgentPointServiceProvider provider = new AgentPointServiceProvider();
        provider.produce();
    }

    private void loadConfig() {
        Properties properties = loadConfigProperties();

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

            if (attrs.length == 2 && StringUtils.equalsIgnoreCase(attrs[0], SERVICE)) {
                if (SERVICE_PORT_ATTR.equalsIgnoreCase(attrs[1])) {
                    if (AgentConfigManager.getInstance().getServicePort() != -1) {
                        throw new RuntimeException("Config 'service.port' already exists.");
                    }
                    AgentConfigManager.getInstance().setServicePort(Integer.parseInt(propValue));
                } else {
                    throw new RuntimeException(errMsg);
                }
            } else if (attrs.length == 2 && StringUtils.equalsIgnoreCase(attrs[0], CLIENT)) {
                if (CLIENT_HOST_ATTR.equalsIgnoreCase(attrs[1])) {
                    if (StringUtils.isNotEmpty(AgentConfigManager.getInstance().getClientHost())) {
                        throw new RuntimeException("Config 'client.host' already exists.");
                    }
                    AgentConfigManager.getInstance().setClientHost(propValue);
                } else if (CLIENT_PORT_ATTR.equalsIgnoreCase(attrs[1])) {
                    if (AgentConfigManager.getInstance().getClientPort() != -1) {
                        throw new RuntimeException("Config 'client.port' already exists.");
                    }
                    AgentConfigManager.getInstance().setClientPort(Integer.parseInt(propValue));
                } else {
                    throw new RuntimeException(errMsg);
                }
            } else {
                throw new RuntimeException(errMsg);
            }
        }

        if (AgentConfigManager.getInstance().getServicePort() == -1) {
            throw new RuntimeException("No 'service.port' configured.");
        }

        if (StringUtils.isEmpty(AgentConfigManager.getInstance().getClientHost())) {
            throw new RuntimeException("No 'client.host' configured.");
        }

        if (AgentConfigManager.getInstance().getClientPort() == -1) {
            throw new RuntimeException("No 'client.port' configured.");
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
