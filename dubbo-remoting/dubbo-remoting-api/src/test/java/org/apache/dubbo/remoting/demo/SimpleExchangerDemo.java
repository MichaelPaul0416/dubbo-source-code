package org.apache.dubbo.remoting.demo;

import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.exchange.ExchangeChannel;
import org.apache.dubbo.remoting.exchange.Exchangers;
import org.apache.dubbo.remoting.exchange.support.Replier;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/3/20 15:01
 * @Description:
 **/
public class SimpleExchangerDemo {

    @Test
    public void exchangerStart(){
        try {
            /**
             * 这里使用{@link org.apache.dubbo.common.extension.Adaptive}来适配
             */

            Exchangers.bind("dubbo://192.168.126.168:9090?transporter=sim", new Replier<String>() {
                @Override
                public Object reply(ExchangeChannel channel, String request) throws RemotingException {
                    System.out.println("request:" + request);
                    return channel;
                }
            });
        } catch (RemotingException e) {
            System.out.println(e.getMessage());
        }
    }
}
