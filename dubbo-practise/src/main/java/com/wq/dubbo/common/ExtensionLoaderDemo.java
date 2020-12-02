package com.wq.dubbo.common;

import com.wq.dubbo.common.spi.ContactInfo;
import com.wq.dubbo.common.spi.JavaLanguage;
import com.wq.dubbo.common.spi.Language;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/2 10:46
 * @Description:
 **/
public class ExtensionLoaderDemo {

    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoaderDemo.class);

    public static void main(String[] args) {
        Language language = ExtensionLoader.getExtensionLoader(Language.class).getAdaptiveExtension();
        // 实现类没有使用@Adaptive注解修饰，所以需要自己生成一个新的类，针对做强化
        logger.info("className:{}", language.getClass().getName());
        logger.info("instance of {}:{}", JavaLanguage.class, language instanceof JavaLanguage);


        URL url = new URL("ns", "127.0.0.1", 8080);
        // key是@Adaptive修饰的接口里面value的值，value就是SPI配置文件里面，key的部分，也就是等号左边
//        url = url.addParameter("version","golang");
        ContactInfo contactInfo = new ContactInfo(url, "self");
        logger.info(language.version(contactInfo));
    }
}
