package com.wq.dubbo.rpc;

import com.wq.dubbo.rpc.api.DateTimeFinder;
import com.wq.dubbo.rpc.api.server.DefaultDateTimeFinder;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

/**
 * 直接使用api的方式配置dubbo环境
 */
public class ApiRpcProvider {

    public static void main(String[] args) {
        DateTimeFinder finder = new DefaultDateTimeFinder();
        ApplicationConfig appConfig = new ApplicationConfig();
        appConfig.setName("example");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:8888");
        registryConfig.setUsername("dubbo");
        registryConfig.setPassword("dubbo");

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(8088);
        protocolConfig.setThreads(10);
        // 使用netty4进行网络交互，默认值netty为netty3版本
        protocolConfig.setServer("netty4");

        // 重对象，封装了与注册中心的链接
        ServiceConfig<DateTimeFinder> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(appConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(DateTimeFinder.class);
        serviceConfig.setRef(finder);
        serviceConfig.setVersion("1.0.0");

        serviceConfig.export();
    }
}
