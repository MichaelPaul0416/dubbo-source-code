package com.wq.dubbo.common.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/2 11:07
 * @Description:
 **/
public class JavaLanguage implements Language {

    private static final Logger logger = LoggerFactory.getLogger(JavaLanguage.class);

    @Override
    public String introduce() {
        return "Java";
    }

    @Override
    public String version(ContactInfo info) {
        logger.info("contactInfo:{}", info);
        return "jdk11";
    }
}
