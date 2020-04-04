package org.apache.dubbo.remoting.http.tomcat;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.http.HttpHandler;
import org.apache.dubbo.remoting.http.HttpServer;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/3 14:27
 * @Description:
 **/
public class HttpDubboTest {

    @Test
    public void tomcatHttpServer() {
        int port = 8080;
        URL url = new URL("http", "127.0.0.1", port,
                new String[]{Constants.BIND_PORT_KEY, String.valueOf(port)});
        StopCaller stopCaller = new StopCaller();
        Thread notify = new Thread(new Runnable() {

            @Override
            public void run() {
                while (stopCaller.isRun()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        });
        notify.start();
        HttpServer tomcat = new TomcatHttpServer(url, new HttpHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                String reqPath = request.getRequestURI();
                System.out.println("req uri:" + reqPath);
                OutputStream outputStream = response.getOutputStream();
                SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = dtf.format(new Date());
                outputStream.write(time.getBytes());
                outputStream.flush();
                stopCaller.stopServer();
            }
        });

        try {
            notify.join();
        } catch (InterruptedException e) {
            System.out.println("监视线程被中断");
        }

        System.out.println("server shutdown...");

    }

    @Test
    public void extendRelation(){
        S s1 = new S();
        S s2 = new S();

        System.out.println(s1);
        System.out.println(s2);

    }

    private static class F {
        protected Object field = new Object();
    }

    private static class S extends F{
        @Override
        public String toString() {
//            return field.toString();
            return super.toString();
        }
    }
    private static class StopCaller {
        private volatile boolean stop = false;

        public void stopServer() {
            if (!stop) {
                stop = true;
            }
        }

        public boolean isRun() {
            return !this.stop;
        }
    }
}
