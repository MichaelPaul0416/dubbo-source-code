package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.utils.StringUtils;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 10:37
 * @Description:
 **/
// 这个实现类是默认的spi实现类
@Adaptive
public class DefaultNetProtocol implements NetProtocol {

    private final ExtensionLoader<NetProtocol> extensionLoader = ExtensionLoader.getExtensionLoader(NetProtocol.class);

    @Override
    public void transfer(String name, URL url) {
        System.out.println("default net protocol");
        if (url == null) {
            return;
        }
        // get real spi name
        String key = url.getParameter("net.protocol");
        if (StringUtils.isNotEmpty(key)) {
            NetProtocol netProtocol = extensionLoader.getExtension(key);
            System.out.println("begin call real method");
            try {
                netProtocol.transfer(name, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("end call real method");
        }
    }

    @Override
    public void printProtocol(String caller) {
        System.out.println("default caller");
    }
}
