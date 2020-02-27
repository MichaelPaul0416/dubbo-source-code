package org.apache.dubbo.remoting.demo;

import org.apache.dubbo.common.Parameters;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 15:47
 * @Description:
 **/
public class SimpleServer implements Server {

    private final ServerSocket serverSocket;

    private final ChannelHandler channelHandler;

    private Channel channel;

    public SimpleServer(ServerSocket ss, ChannelHandler handler) {
        this.serverSocket = ss;
        this.channelHandler = handler;
    }


    public Socket accept(){
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean isBound() {
        return this.serverSocket.isBound();
    }

    @Override
    public Collection<Channel> getChannels() {
        return Collections.singleton(this.channel);
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        InetSocketAddress remote = channel.getRemoteAddress();
        if (!(remote.getHostString().equals(remoteAddress.getHostString())
                && remote.getPort() == remoteAddress.getPort())) {
            return null;
        }
        return this.channel;
    }

    @Override
    public void reset(Parameters parameters) {
        // do nothing
    }

    @Override
    public void reset(URL url) {

    }

    @Override
    public URL getUrl() {
        return this.channel.getUrl();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) this.serverSocket.getLocalSocketAddress();
    }

    @Override
    public void send(Object message) throws RemotingException {
        this.channel.send(message);
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        this.channel.send(message, sent);
    }

    @Override
    public void close() {
        this.channel.close();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(int timeout) {
        this.channel.close(timeout);
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startClose() {
        this.channel.startClose();
    }

    @Override
    public boolean isClosed() {
        return this.channel.isClosed();
    }
}
