package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.URL;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 10:13
 * @Description:
 **/
public class NettyProtocol implements NetProtocol {
    @Override
    public void transfer(String name, URL url) {
        System.out.println("netty protocol:" + name);
    }

    @Override
    public void printProtocol(String caller) {
        System.out.println("netty caller");
    }
}
