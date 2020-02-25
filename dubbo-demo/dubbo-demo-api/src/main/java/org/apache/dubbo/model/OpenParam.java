package org.apache.dubbo.model;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/5/15
 * @Description:
 * @Resource:
 */
public class OpenParam implements Serializable {

    private static final Long serialVersionUI = 1L;

    private String requestParam;

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
}
