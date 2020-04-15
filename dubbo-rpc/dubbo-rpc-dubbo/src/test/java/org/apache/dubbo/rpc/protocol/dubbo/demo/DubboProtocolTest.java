package org.apache.dubbo.rpc.protocol.dubbo.demo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.ProxyFactory;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/15 9:51
 * @Description:
 **/
public class DubboProtocolTest {

    @Test
    public void dubboProtocol(){
        // 指定协议和代理对象
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

        // 指定接口
        RpcService rpcService = new RpcServiceImpl();

        // 获取Invoker
//        proxyFactory.getInvoker()
        // 协议暴露
//        protocol.export()

    }

}
