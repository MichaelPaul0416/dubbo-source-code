package org.apache.dubbo.common.extensionloader.demo;

public class SpringParser implements FrameParser{
    @Override
    public void doParse(String condition) {
        System.out.println("spring framework parser:"+ condition);
    }
}
