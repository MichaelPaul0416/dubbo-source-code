package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI
public interface FrameParser {

    @Adaptive
    void doParse(String condition);
}
