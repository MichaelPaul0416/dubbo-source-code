package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI
public interface NetProtocol {

    /**
     * 适配接口，参数中需要带一个{@link URL}
     * @param name
     * @param url
     */
    @Adaptive
    void transfer(String name, URL url);

    /**
     * 普通SPI接口方法
     * 注意，如果获得的是适配对象，那么该对象中除了带{@link Adaptive}注解的方法可以使用，其他方法调用都会报错
     * interface xxx is not adaptive method!
     * @param caller
     */
    void printProtocol(String caller);
}
