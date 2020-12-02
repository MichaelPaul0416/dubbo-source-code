package com.wq.dubbo.common.spi;

/**
 * @Author: wangqiang20995
 * @Date: 2020/12/2 11:07
 * @Description:
 **/
public class GolangLanguage implements Language {
    @Override
    public String introduce() {
        return "golang";
    }

    @Override
    public String version(ContactInfo info) {
        return "1.2.16";
    }
}
