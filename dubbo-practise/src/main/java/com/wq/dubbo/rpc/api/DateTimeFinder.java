package com.wq.dubbo.rpc.api;

import org.apache.dubbo.common.config.AsyncFor;

@AsyncFor(value = DateTimeFinder.class)
public interface DateTimeFinder {

    String currentTime(String format);
}
