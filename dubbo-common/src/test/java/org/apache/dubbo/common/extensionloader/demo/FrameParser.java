package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface FrameParser {

    void doParse(String condition);
}
