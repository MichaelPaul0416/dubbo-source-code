package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.support.ProtocolUtils;
import org.junit.Test;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/9 16:54
 * @Description:
 **/
public class ProtocolUtilsTest {

    @Test
    public void testServiceKey(){
        URL url = new URL("dubbo","192.168.153.103",8080)
                .addParameter(Constants.BIND_PORT_KEY,"9999")
                .addParameter(Constants.VERSION_KEY,"123")
                .addParameter(Constants.GROUP_KEY,"db")
                .setPath("com.dubbo.demo.protocol");

        System.out.println(ProtocolUtils.serviceKey(url));
    }
}
