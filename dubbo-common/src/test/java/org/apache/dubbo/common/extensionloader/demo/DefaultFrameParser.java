package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.extension.Adaptive;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/24 13:16
 * @Description:
 **/

@Adaptive
public class DefaultFrameParser implements FrameParser {
    @Override
    public void doParse(String condition) {
        System.out.println("default frame parser for condition:" + condition);
    }
}
