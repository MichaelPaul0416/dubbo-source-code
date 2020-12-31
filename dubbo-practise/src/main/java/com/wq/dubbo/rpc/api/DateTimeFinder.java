package com.wq.dubbo.rpc.api;

import java.util.concurrent.CompletableFuture;

public interface DateTimeFinder {

    String currentTime(String format);

    void hello(String kkk);

    CompletableFuture<String> printTime(String tip);

}
