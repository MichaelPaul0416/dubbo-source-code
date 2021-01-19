package com.wq.dubbo.rpc.api.server;

import com.wq.dubbo.rpc.api.DateTimeFinder;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class DefaultDateTimeFinder implements DateTimeFinder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDateTimeFinder.class);

    private static final ThreadLocal<DateFormat> safeFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat());

    @Override
    public String currentTime(String format) {
        String f = StringUtils.isBlank(format) ? "yyyyMMddHHmmss" : format;
        logger.info("format:{}", f);
        DateFormat dateFormat = safeFormatter.get();
        return dateFormat.format(new Date());
    }

    @Override
    public void hello(String kkk) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------->" + kkk);
        throw new RuntimeException("throw exception while return type is void");
    }

    @Override
    public CompletableFuture<String> printTime(String tip) {
        RpcContext rpcContext = RpcContext.getContext();
        System.out.println("Asyn --------> " + tip);
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(rpcContext.getAttachments().values());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "async response from provider";
        });
    }
}
