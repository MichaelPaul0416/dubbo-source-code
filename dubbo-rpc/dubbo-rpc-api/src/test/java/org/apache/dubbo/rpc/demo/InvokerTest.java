package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 13:41
 * @Description:
 **/
public class InvokerTest {

    private static class SocketInvoker<T> implements Invoker<T>{

        private Class<T> service;

        public SocketInvoker(Class<T> service){
            this.service = service;
        }

        @Override
        public Class<T> getInterface() {
            return this.service;
        }

        @Override
        public Result invoke(Invocation invocation) throws RpcException {
            return null;
        }

        @Override
        public URL getUrl() {
            return null;
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public void destroy() {

        }
    }
}
