package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.URL;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 10:08
 * @Description:
 **/
public class StringProtocol implements NetProtocol {
    @Override
    public void transfer(String name, URL url) {
        System.out.println("StringProtocol");
    }

    @Override
    public void printProtocol(String caller) {
        System.out.println("string caller");
    }
}
