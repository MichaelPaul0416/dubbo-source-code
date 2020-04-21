package org.apache.dubbo.rpc.protocol.dubbo.demo;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/21 16:50
 * @Description:
 **/
@Activate(group = Constants.PROVIDER,order = -1)
public class SensitiveFilter implements Filter {

    private static final Set<String> keys;

    static {
        keys = new HashSet<>();
        keys.add("password");
        keys.add("id_no");
    }

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object[] attachments = invocation.getArguments();
        for (Object obj : attachments) {
            if (keys.contains(obj)) {
                logger.info("sensitive message:" + obj + "/" +invocation.getAttachment((String) obj));
            }
        }

        return invoker.invoke(invocation);
    }
}
