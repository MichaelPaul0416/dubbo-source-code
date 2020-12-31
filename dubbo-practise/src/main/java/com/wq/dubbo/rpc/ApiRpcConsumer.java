package com.wq.dubbo.rpc;

import com.wq.dubbo.rpc.api.AsyncService;
import com.wq.dubbo.rpc.api.DateTimeFinder;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/15 13:36
 * @Description:
 **/
public class ApiRpcConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ApiRpcConsumer.class);

    private static volatile boolean done = false;

    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("exampleConsumer");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setUsername("");
        registryConfig.setPassword("");
        registryConfig.setProtocol("zookeeper");

//        testDateTimeFinderSyncRpc(applicationConfig, registryConfig);

        testWithAsyncRpcCall(applicationConfig, registryConfig);
    }

    private static void testWithAsyncRpcCall(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ReferenceConfig<AsyncService> asyncConfig = configRefProvider(AsyncService.class, applicationConfig, registryConfig);

        // 直接设置接口是异步的
        setRpcCallAsyn(asyncConfig);

        // 设置方法级别的异步调用
//        setAsyncWithMethodConfig(asyncConfig);

        AsyncService asyncService = asyncConfig.get();

        // 直接从结果集获取
//        getFutureFromCallReturn(asyncService);

        // 从上下文获取当前的CompletableFuture
        getFutureFromContext(asyncService);

        logger.info("call done and waiting for result");

        while (!done){
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                //
            }
        }

        logger.info("receive result from remote completely and shutdown now");
    }

    private static void allDone(){
        ApiRpcConsumer.done = true;
    }

    /**
     * 设置{@link ReferenceConfig#setAsync(Boolean)}
     * @param asyncConfig
     */
    private static void setRpcCallAsyn(ReferenceConfig<AsyncService> asyncConfig) {
        asyncConfig.setAsync(true);
    }

    /**
     * 通过{@link MethodConfig}设置方法级别的参数
     * @param asyncConfig
     */
    private static void setAsyncWithMethodConfig(ReferenceConfig<AsyncService> asyncConfig) {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        // 通过设置方法为异步方法
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("sayHello");
        methodConfig.setAsync(true);
        methodConfigs.add(methodConfig);
        asyncConfig.setMethods(methodConfigs);
    }

    /**
     * 异步调用的结果是{@link CompletableFuture}
     * @param asyncService
     */
    private static void getFutureFromCallReturn(AsyncService asyncService) {
        CompletableFuture<String> future = asyncService.sayHello("async");
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                logger.info("async response:{}",result);
            }
            allDone();
        });
    }

    /**
     * 从{@link RpcContext#getContext()}获取当前上下文
     * @param asyncService
     */
    private static void getFutureFromContext(AsyncService asyncService) {
        asyncService.sayHello("async");
        RpcContext.getContext().getCompletableFuture().whenComplete((v, t) -> {
            if (t != null) {
                // error
                t.printStackTrace();
            } else {
                logger.info("response->{}", v);
            }
            allDone();
        });
    }

    /**
     * 同步功能调用测试
     * @param applicationConfig
     * @param registryConfig
     */
    private static void testDateTimeFinderSyncRpc(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ReferenceConfig<DateTimeFinder> dateConfig = configRefProvider(DateTimeFinder.class, applicationConfig, registryConfig);
        DateTimeFinder dateTimeFinder = dateConfig.get();
        logger.info(dateTimeFinder.currentTime("yyyyMMddHHmmss"));
        long start = System.currentTimeMillis();
        dateTimeFinder.hello("sdfuibfui");
        logger.info("done:{}", (System.currentTimeMillis() - start));
    }

    /**
     * 生成并且配置{@link ReferenceConfig}
     * @param cla
     * @param applicationConfig
     * @param registryConfig
     * @param <T>
     * @return
     */
    private static <T> ReferenceConfig<T> configRefProvider(Class<T> cla, ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ReferenceConfig<T> config = new ReferenceConfig<>();
        config.setApplication(applicationConfig);
        config.setRegistry(registryConfig);
        config.setInterface(cla);
        config.setVersion("1.0.0");

        return config;
    }
}
