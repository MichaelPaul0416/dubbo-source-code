package org.apache.dubbo.remoting.demo;

import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.Transporters;
import org.apache.dubbo.remoting.transport.ChannelHandlerAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
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

    @Test
    public void bindAddressToRemote(){
        try {
            ServerSocket ss = new ServerSocket();
            // bind remote address -> failed
            ss.bind(new InetSocketAddress("192.168.56.102",9999));

            while (true){
                Socket socket = ss.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream()),"UTF-8"));
                System.out.println(br.readLine());

                socket.getOutputStream().write("hello world".getBytes("UTF-8"));
                socket.getOutputStream().flush();

                socket.close();
                break;
            }

            ss.close();
        } catch (IOException e) {
            String s = e.getMessage();
            Assert.assertEquals("Cannot assign requested address: JVM_Bind",s);
//            e.printStackTrace();
        }
    }
}
