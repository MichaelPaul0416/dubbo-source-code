package org.apache.dubbo.remoting.demo;

import net.sf.cglib.core.Local;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.Transporters;
import org.apache.dubbo.remoting.transport.ChannelHandlerAdapter;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/27 14:39
 * @Description:
 **/
public class AddressDemo {

    @Test
    public void showAllNetWorkInterface() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                Enumeration<InetAddress> inetAddress = ni.getInetAddresses();
                while (inetAddress.hasMoreElements()) {
                    InetAddress address = inetAddress.nextElement();
                    System.out.println(address.getHostAddress());
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void showCode() {
        System.out.println("->" + new String(new byte[]{32, 8}));
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void simpleChannelServer() {
        try {
            SimpleServer simpleServer = (SimpleServer) Transporters.bind("sim://192.168.126.168:9999?server=sim", new ChannelHandlerAdapter());

            while (true) {
                Socket socket = simpleServer.accept();
                SimpleChannel simpleChannel = new SimpleChannel(socket, null);
                simpleChannel.send("hello client..." + LocalDateTime.now());
                simpleChannel.close();
            }
        } catch (RemotingException e) {
            e.printStackTrace();
        }

    }
}
