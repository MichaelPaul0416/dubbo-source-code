package org.apache.dubbo.model;

/**
 * @Author: wangqiang20995
 * @Date:2019/5/15
 * @Description:
 * @Resource:
 */
public enum ResultStatus {

    SUCCESS((byte) 1),

    FAILED((byte) 0);

    ResultStatus(byte status){
        this.status = status;
    }

    private byte status;
}
