package org.apache.dubbo.rpc.protocol.dubbo.demo;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/15 9:52
 * @Description:
 **/
public class RpcServiceImpl implements RpcService {
    @Override
    public String doRpc(String args) {
        return "rpc server response:" + args;
    }
}
