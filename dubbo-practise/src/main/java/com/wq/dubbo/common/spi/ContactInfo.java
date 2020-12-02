package com.wq.dubbo.common.spi;

import org.apache.dubbo.common.URL;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/2 13:58
 * @Description:
 **/
public class ContactInfo {

    private URL url;

    private String name;

    @Override
    public String toString() {
        return "ContactInfo{" +
                "url=" + url +
                ", name='" + name + '\'' +
                '}';
    }

    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public ContactInfo(URL url, String name) {
        this.url = url;
        this.name = name;
    }
}
