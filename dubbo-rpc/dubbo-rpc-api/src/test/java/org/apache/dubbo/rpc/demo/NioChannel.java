package org.apache.dubbo.rpc.demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.transport.AbstractPeer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 14:12
 * @Description:
 **/
public class NioChannel implements Channel {

    public static final String PROTOCOL = "nio";

    private final SocketChannel socketChannel;
    private final Map<String, Object> channelAttr = new ConcurrentHashMap<>(8);
    private final URL url;
    private final Class<?> clazz;
    private final ChannelHandler nioChannelHandler;
    private final AbstractPeer peer;

    public NioChannel(ChannelHandler channelHandler, SocketChannel socketChannel, Class<?> clazz,AbstractPeer peer) {
        this.socketChannel = socketChannel;
        this.clazz = clazz;
        InetSocketAddress ias = getRemoteAddress();
        this.url = new URL(PROTOCOL, ias.getHostString(), ias.getPort(), this.clazz.getName());
        this.nioChannelHandler = channelHandler;
        this.peer = peer;

    }

    public ChannelHandler channelHandler(){
        return this.nioChannelHandler;
    }

    private String buildPath(Class<?> service) {
        StringBuilder builder = new StringBuilder();
        for (Class cla : service.getInterfaces()) {
            builder.append(cla.getName()).append(",");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        try {
            SocketAddress address = this.socketChannel.getRemoteAddress();
            return (InetSocketAddress) address;
        } catch (IOException e) {
            System.out.println("获取远程地址异常:" + e.getMessage());
            throw new RuntimeException("获取远程地址异常:" + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return this.channelAttr.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return this.channelAttr.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.channelAttr.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        this.channelAttr.remove(key);
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.nioChannelHandler;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        try {
            return (InetSocketAddress) this.socketChannel.getLocalAddress();
        } catch (IOException e) {
            System.out.println("获取本地地址信息异常:" + e.getMessage());
            throw new RuntimeException("获取本地地址信息异常", e);
        }
    }

    @Override
    public void send(Object message) throws RemotingException {
        send(message,false);
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        String json = JSONObject.toJSONString(message);
        try {
            this.socketChannel.write(ByteBuffer.wrap(json.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            System.out.println("关闭链路异常:" + getRemoteAddress() + "/" + e.getMessage());
        }
    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public void startClose() {
        this.peer.startClose();
    }

    @Override
    public boolean isClosed() {
        return !this.socketChannel.isConnected();
    }

    public static boolean basicType(Object message){
        return message instanceof String || message instanceof Long || message instanceof Short
                || message instanceof Double || message instanceof Float || message instanceof Character
                || message instanceof Byte || message instanceof Boolean || message instanceof Integer;
    }

}
