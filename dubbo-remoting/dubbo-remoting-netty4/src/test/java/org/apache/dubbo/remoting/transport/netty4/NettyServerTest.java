package org.apache.dubbo.remoting.transport.netty4;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/3 11:13
 * @Description:
 **/
public class NettyServerTest {

    @Test
    public void startNettyServer() throws RemotingException {
        URL url = URL.valueOf("dubbo://127.0.0.1:9898");
        // 一般来说，这个ChannelHandler是真正在Server上处理的ChannelHandler
        NettyServer nettyServer = new NettyServer(url, new ChannelHandler() {
            @Override
            public void connected(Channel channel) throws RemotingException {
                System.out.println("receive a connection from remote:" + NetUtils.toAddressString(channel.getRemoteAddress()));
            }

            @Override
            public void disconnected(Channel channel) throws RemotingException {
                System.out.println("disconnected from remote:" + NetUtils.toAddressString(channel.getRemoteAddress()));
            }

            @Override
            public void sent(Channel channel, Object message) throws RemotingException {
                System.out.println("send message:" + message);
                channel.send(message);
            }

            @Override
            public void received(Channel channel, Object message) throws RemotingException {
                System.out.println("receive message:" + message);
                channel.send("1234567890");
            }

            @Override
            public void caught(Channel channel, Throwable exception) throws RemotingException {
                System.out.println(exception.getMessage());
            }
        });
        System.out.println("start ok");
        while (true){

        }
    }

    @Test
    public void startNettyClient() throws RemotingException {
        URL url = URL.valueOf("dubbo://127.0.0.1:9898");
        NettyClient nettyClient = new NettyClient(url, new ChannelHandler() {
            @Override
            public void connected(Channel channel) throws RemotingException {
                System.out.println("connect to server done:" + NetUtils.toAddressString(channel.getRemoteAddress()));
            }

            @Override
            public void disconnected(Channel channel) throws RemotingException {

            }

            @Override
            public void sent(Channel channel, Object message) throws RemotingException {
                System.out.println("send message:" + message);
                channel.send(message);
            }

            @Override
            public void received(Channel channel, Object message) throws RemotingException {
                System.out.println("receive response from remote:" + message);
                System.out.println("shutdown channel:" + channel);
                channel.close();
            }

            @Override
            public void caught(Channel channel, Throwable exception) throws RemotingException {
                System.out.println("error:" + exception.getMessage());
            }
        });
        nettyClient.send("client request");
        while (nettyClient.isConnected()){

        }
    }
}
