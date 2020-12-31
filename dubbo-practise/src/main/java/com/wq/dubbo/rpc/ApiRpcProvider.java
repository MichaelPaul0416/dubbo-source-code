package com.wq.dubbo.rpc;

import com.wq.dubbo.rpc.api.AsyncService;
import com.wq.dubbo.rpc.api.DateTimeFinder;
import com.wq.dubbo.rpc.api.server.AsyncServiceImpl;
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
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setUsername("");
        registryConfig.setPassword("");
        registryConfig.setProtocol("zookeeper");

        ProtocolConfig protocolConfig = new ProtocolConfig();
//        protocolConfig.setName("injvm");
        protocolConfig.setPort(8088);
        protocolConfig.setThreads(10);
        // 使用netty4进行网络交互，默认值netty为netty3版本
        protocolConfig.setServer("netty4");

        // 重对象，封装了与注册中心的链接
        ServiceConfig<DateTimeFinder> serviceConfig = generateServiceConfig(finder, DateTimeFinder.class, appConfig, registryConfig, protocolConfig);
        serviceConfig.export();

        AsyncService service = new AsyncServiceImpl();
        ServiceConfig<AsyncService> asyncConfig = generateServiceConfig(service, AsyncService.class, appConfig, registryConfig, protocolConfig);
        asyncConfig.export();

        while (true) {
        }
    }

    private static <T> ServiceConfig<T> generateServiceConfig(T instance, Class<T> inter,
                                                              ApplicationConfig appConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig) {
        ServiceConfig<T> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(appConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(inter);
        serviceConfig.setRef(instance);
        serviceConfig.setVersion("1.0.0");
        return serviceConfig;
    }
}
