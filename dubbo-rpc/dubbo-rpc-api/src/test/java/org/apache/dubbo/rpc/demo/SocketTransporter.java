package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.*;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 13:45
 * @Description:
 **/
public class SocketTransporter implements Transporter {

    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        return null;
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        return null;
    }
}
