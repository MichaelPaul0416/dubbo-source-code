package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/13 14:18
 * @Description:
 **/
public class SimpleRpcFrameworkClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRpcFrameworkClientTest.class);

    public static void main(String[] args) {
//        SocketTransporter socketTransporter = new SocketTransporter();
//        URL url = new URL(NioChannel.PROTOCOL, "localhost", 9090, "rpc-client");
//        try {
//            NioClient nioClient = (NioClient) socketTransporter.connect(url, new ChannelHandler() {
//                @Override
//                public void connected(Channel channel) throws RemotingException {
//                    logger.info("connected to remote:" + channel.getRemoteAddress());
//                }
//
//                @Override
//                public void disconnected(Channel channel) throws RemotingException {
//                    logger.info("disconnected from remote:" + channel.getRemoteAddress());
//                }
//
//                @Override
//                public void sent(Channel channel, Object message) throws RemotingException {
//                    channel.send(message);
//                }
//
//                @Override
//                public void received(Channel channel, Object message) throws RemotingException {
//                    logger.info("receive:" + message.toString());
//
//                }
//
//                @Override
//                public void caught(Channel channel, Throwable exception) throws RemotingException {
//                    logger.error(exception.getMessage(), exception);
//                    channel.close();
//                }
//            });
//
//            nioClient.send("hello server");
//        } catch (RemotingException e) {
//            logger.error(e.getMessage(), e);
//        }

        commonNio();
    }

    private static void commonNio() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            Selector selector = Selector.open();

            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);

            boolean ok = socketChannel.connect(new InetSocketAddress("localhost",9090));
            logger.info("connect result:" + ok);

            socketChannel.write(ByteBuffer.wrap("hello world".getBytes()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
