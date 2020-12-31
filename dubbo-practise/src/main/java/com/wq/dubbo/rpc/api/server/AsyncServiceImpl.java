package com.wq.dubbo.rpc.api.server;

import com.wq.dubbo.rpc.api.AsyncService;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/30 15:14
 * @Description:
 **/
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Override
    public CompletableFuture<String> sayHello(String name) {
        RpcContext saveContext = RpcContext.getContext();
        return CompletableFuture.supplyAsync(() -> {
            logger.info("obtain parameter:{}", name);
            return "async reponse from provider";
        });
    }
}
