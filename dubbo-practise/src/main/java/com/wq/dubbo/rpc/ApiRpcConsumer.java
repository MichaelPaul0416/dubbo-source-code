package com.wq.dubbo.rpc;

import com.wq.dubbo.rpc.api.DateTimeFinder;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/15 13:36
 * @Description:
 **/
public class ApiRpcConsumer {
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("exampleConsumer");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setUsername("");
        registryConfig.setPassword("");
        registryConfig.setProtocol("zookeeper");

        ReferenceConfig<DateTimeFinder> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(DateTimeFinder.class);
        referenceConfig.setVersion("1.0.0");

        DateTimeFinder dateTimeFinder = referenceConfig.get();
        System.out.println(dateTimeFinder.currentTime("yyyyMMddHHmmss"));
    }
}
