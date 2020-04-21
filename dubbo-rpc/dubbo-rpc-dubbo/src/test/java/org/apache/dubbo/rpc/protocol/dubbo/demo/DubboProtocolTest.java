package org.apache.dubbo.rpc.protocol.dubbo.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.ProtocolFilterWrapper;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/15 9:51
 * @Description:
 **/
public class DubboProtocolTest {

    private Protocol protocol;
    private ProxyFactory proxyFactory;
    private RpcService rpcService;

    @Before
    public void prepare(){
        // 指定协议和代理对象
        this.protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        this.proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
        this.rpcService = new RpcServiceImpl();
    }

    @Test
    public void dubboProtocol() {

        URL url = new URL("dubbo", "192.168.153.103", 9999);
        // 获取Invoker
        Invoker<RpcService> invoker = proxyFactory.getInvoker(rpcService, RpcService.class, url);

        // 协议暴露
        protocol.export(invoker);

        // client操作
        Invoker<RpcService> remoteInvoker = protocol.refer(RpcService.class, url);

        // 代理获取接口代理对象，进行远程调用
        RpcService proxyService = proxyFactory.getProxy(remoteInvoker);
        String str = proxyService.doRpc("hello");
        System.out.println("rpc call result:" + str);

        // 也可以直接使用Invoker进行调用
        RpcInvocation invocation = new RpcInvocation();
        invocation.setInvoker(remoteInvoker);
        invocation.setArguments(new String[]{"hello"});
        invocation.setParameterTypes(new Class[]{String.class});
        invocation.setMethodName("doRpc");
        Result result = remoteInvoker.invoke(invocation);
        System.out.println("invoker result -> " + result.getValue());

        while (true){

        }
    }

    @Test
    public void protocolWithFilter(){
        URL url = new URL("dubbo","192.168.153.103",9090);
        // 一般来说getInvoker是针对server的，getProxy是针对client的
        Invoker<RpcService> invoker = proxyFactory.getInvoker(rpcService,RpcService.class,url);
        ProtocolFilterWrapper filterWrapper = new ProtocolFilterWrapper(protocol);
        filterWrapper.export(invoker);

        Invoker<RpcService> clientRpc = protocol.refer(RpcService.class,url);
        RpcService client = proxyFactory.getProxy(clientRpc);

        System.out.println(client.doRpc("hello with filter"));

        while (true){

        }
    }

}
