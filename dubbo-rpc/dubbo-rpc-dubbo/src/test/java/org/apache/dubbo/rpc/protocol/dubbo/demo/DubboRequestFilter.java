package org.apache.dubbo.rpc.protocol.dubbo.demo;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/21 16:47
 * @Description:
 **/
@Activate(group = Constants.PROVIDER,order = -2)
public class DubboRequestFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(DubboRequestFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.info("dubbo request:[" + invocation.getMethodName() + "] in class -> {" + invocation.getInvoker().getInterface() + "}");
        return invoker.invoke(invocation);
    }
}
