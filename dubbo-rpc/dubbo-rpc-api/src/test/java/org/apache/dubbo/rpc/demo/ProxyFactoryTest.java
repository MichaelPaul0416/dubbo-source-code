package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.ProxyFactory;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcInvocation;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/15 9:57
 * @Description:
 **/
public class ProxyFactoryTest {

    public static interface InnerFace{
        void show(String num);
    }

    @Test
    public void showProxy(){
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

        Invoker<InnerFace> innerFaceInvoker = proxyFactory.getInvoker(new InnerFace() {
            @Override
            public void show(String num) {
                System.out.println("num->" + num);
            }
        },InnerFace.class,new URL("dubbo","192.168.153.103",9999));

        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setMethodName("show");
        rpcInvocation.setParameterTypes(new Class[]{String.class});
        rpcInvocation.setArguments(new String[]{"hello"});

        Result result = innerFaceInvoker.invoke(rpcInvocation);
        System.out.println(result.getValue());
    }
}
