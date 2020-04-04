package org.apache.dubbo.common.serialize.serialization;

import com.alibaba.com.caucho.hessian.io.HessianProtocolException;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.common.serialize.hessian2.Hessian2Serialization;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/4 16:50
 * @Description:
 **/
public class SimpleSerializationTest {

    private Serialization serialization;

    @Before
    public void prepare() throws HessianProtocolException {
        serialization = new Hessian2Serialization();
    }

    @Test
    public void codecCompositeObj(){
        Bean bean = new Bean();
        System.out.println(bean);
        URL url = new URL("netty","192.168.0.101",8080,new String[]{"one","two"});
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;
        try {
            ObjectOutput dataOut = serialization.serialize(url,baos);
            dataOut.writeObject(bean);

            byte[] buffer = baos.toByteArray();
            System.out.println("serialization size:" + buffer.length);

            // deserialize
            bais = new ByteArrayInputStream(buffer);
            ObjectInput dataInput = serialization.deserialize(url,bais);
            Bean copy = dataInput.readObject(Bean.class);
            System.out.println(copy);

            System.out.println("equals:" + copy.equals(bean));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class Bean implements Serializable {
        private String name;
        private Map<String, List<Calendar>> calenderMap;
        private String password;

        public Bean(){
            this.name = "HashCode";
            this.calenderMap = new ConcurrentHashMap<>();
            Calendar temp = Calendar.getInstance();
            temp.add(Calendar.YEAR,3);
            List<Calendar> list = Arrays.asList(new Calendar[]{Calendar.getInstance(),temp});
            calenderMap.put("list",list);
            this.password = "123456";
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", calenderMap=" + calenderMap +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
