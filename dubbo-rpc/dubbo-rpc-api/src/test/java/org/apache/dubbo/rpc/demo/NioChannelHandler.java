package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 14:26
 * @Description:
 **/
public class NioChannelHandler implements ChannelHandler {

    @Override
    public void connected(Channel channel) throws RemotingException {
        System.out.println("与远程[" + channel.getRemoteAddress() + "]建立链接...");
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        System.out.println("与远程["+channel.getRemoteAddress()+"]断开链接");
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {
        System.out.println("向通道["+channel.getRemoteAddress()+"]发送消息");
        channel.send(message);
    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {
        System.out.println("从通道["+channel.getRemoteAddress()+"]接收消息");
        channel.send("receive ok");
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        channel.send("内部异常:" + exception.getMessage());
    }
}
