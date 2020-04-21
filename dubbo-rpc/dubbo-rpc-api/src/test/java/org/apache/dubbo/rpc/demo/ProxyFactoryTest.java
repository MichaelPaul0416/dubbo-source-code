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

    @Test
    public void add(){
        int a = 2;
        /**
         * a++ 此时获取的值a=2
         * 第一个++a 运算之前，a经过a++之后，此时为3，所以++a=4
         * 同上，++a=5
         * 最后就是2 << 4+5
         */
        int b = a++ << ++a + ++a;
        System.out.println(b);
        int a1 = 2;
        int b1 = ++a1 + ++a1;
        System.out.println(b1);
    }
}
