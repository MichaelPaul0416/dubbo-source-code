package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/13 13:46
 * @Description:
 **/
public class SimpleRPCFrameTest {

    private static SocketTransporter socketTransporter = new SocketTransporter();

    private static final Logger logger = LoggerFactory.getLogger(SimpleRPCFrameTest.class);

    public static void main(String[] args){
        URL bind = new URL(NioChannel.PROTOCOL,"127.0.0.1",9090,"nio-server-rpc");
        try {
            socketTransporter.bind(bind, new ChannelHandler() {
                @Override
                public void connected(Channel channel) throws RemotingException {
                    logger.info("receive a client connection:" + channel.getRemoteAddress());
                }

                @Override
                public void disconnected(Channel channel) throws RemotingException {
                    logger.info("disconnected from remote:" + channel.getRemoteAddress());
                }

                @Override
                public void sent(Channel channel, Object message) throws RemotingException {
                    logger.info("rpc server -> send message");
                    channel.send(message);
                }

                @Override
                public void received(Channel channel, Object message) throws RemotingException {
                    logger.info("receive message from remote:" + channel.getRemoteAddress());
                    logger.info(message.toString());
                }

                @Override
                public void caught(Channel channel, Throwable exception) throws RemotingException {
                    logger.error(exception.getMessage(),exception);
                }
            });
        } catch (RemotingException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
