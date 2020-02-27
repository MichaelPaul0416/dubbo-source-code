package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extensionloader.ext2.UrlHolder;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/24 13:31
 * @Description:
 **/
public class ExtensionLoaderTest {

    @Test
    public void chooseSpiInstance(){
        ExtensionLoader<FrameParser> extensionLoader = ExtensionLoader.getExtensionLoader(FrameParser.class);
        FrameParser frameParser = extensionLoader.getExtension("def");
        frameParser.doParse("java");
    }

    @Test
    public void adaptive(){
        // https://www.jianshu.com/p/dc616814ce98
        ExtensionLoader<NetProtocol> extensionLoader = ExtensionLoader.getExtensionLoader(NetProtocol.class);
        NetProtocol adaptive = extensionLoader.getAdaptiveExtension();

        URL url = URL.valueOf("str://127.0.0.1:80?net.protocol=netty");
        adaptive.transfer("netty",url);

        /**
         * 适配对象，如果调用没有{@link org.apache.dubbo.common.extension.Adaptive}注解修饰的方法
         * 那么就会报错
         */
        try {
            adaptive.printProtocol("mine");
        }catch (Throwable e){
            assertThat(e.getMessage(), containsString("method "));
            assertThat(
                    e.getMessage(),
                    containsString("of interface org.apache.dubbo.common.extensionloader.demo.NetProtocol is not adaptive method!"));
        }
    }

    @Test
    public void implClassWithAdaptive(){
        /**
         * 接口{@link NetProtocol#transfer(String, URL)}有{@link org.apache.dubbo.common.extension.Adaptive}注解修饰
         * 同时类{@link DefaultNetProtocol}也有注解{@link org.apache.dubbo.common.extension.Adaptive}修饰
         * 默认的话，是先使用类的
         */
        ExtensionLoader<NetProtocol> extensionLoader = ExtensionLoader.getExtensionLoader(NetProtocol.class);
        NetProtocol defaultAdaptive = extensionLoader.getAdaptiveExtension();
        defaultAdaptive.transfer("hello",null);

        // 使用@Adaptive修饰的类实例，获取指定的SPI实现
        URL url = URL.valueOf("netty://localhost:8080?net.protocol=netty");
        defaultAdaptive.transfer("netty framework",url);

        // 使用@Adaptive注解修饰的类，SPI接口中非@Adaptive方法，也可以照样使用
        defaultAdaptive.printProtocol("mine");

    }
}
