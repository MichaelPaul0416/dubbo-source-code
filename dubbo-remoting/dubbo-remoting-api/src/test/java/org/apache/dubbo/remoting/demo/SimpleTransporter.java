package org.apache.dubbo.remoting.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.remoting.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 15:38
 * @Description:
 **/
public class SimpleTransporter implements Transporter {

    private ServerSocket serverSocket;

    public SimpleTransporter() {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            //
        }
    }

    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        int port = url.getPort();
        String host = url.getHost();
        if (NetUtils.isInvalidLocalHost(host)) {
            throw new IllegalArgumentException("无效的主机");
        }

        try {
            serverSocket.bind(new InetSocketAddress(host, port));
            System.out.println("server start ok...");
        } catch (IOException e) {
            System.out.println("bind failed:" + e.getMessage());
        }

        return new SimpleServer(this.serverSocket,handler);
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        return null;
    }
}
