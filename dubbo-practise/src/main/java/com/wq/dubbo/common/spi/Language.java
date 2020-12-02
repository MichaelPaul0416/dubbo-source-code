package com.wq.dubbo.common.spi;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI("java")
public interface Language {

    String introduce();

    @Adaptive("version")
    String version(ContactInfo contactInfo);
}
