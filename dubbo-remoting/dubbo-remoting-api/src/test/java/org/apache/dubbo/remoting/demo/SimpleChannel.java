package org.apache.dubbo.remoting.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 15:58
 * @Description:简单通道
 **/
public class SimpleChannel implements Channel {

    private Socket socket;

    private final URL url;

    private final ChannelHandler channelHandler;

    public SimpleChannel(Socket socket, ChannelHandler channelHandler) {
        this.socket = socket;
        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
        this.url = new URL("sim", isa.getHostString(), isa.getPort());
        this.channelHandler = channelHandler;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) socket.getRemoteSocketAddress();
    }

    @Override
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return false;
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public void removeAttribute(String key) {

    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) this.socket.getLocalSocketAddress();
    }

    @Override
    public void send(Object message) throws RemotingException {
        try (OutputStream os = this.socket.getOutputStream()) {
            String str = String.valueOf(message);
            os.write(str.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        if (sent) {
            System.out.println("already send,and do not send again:" + message);
            return;
        }


        try (OutputStream os = this.socket.getOutputStream()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startClose() {
        System.out.println("start to close socket");
    }

    @Override
    public boolean isClosed() {
        return this.socket.isClosed();
    }
}
