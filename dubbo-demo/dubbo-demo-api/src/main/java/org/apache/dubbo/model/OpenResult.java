package org.apache.dubbo.model;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/5/15
 * @Description:
 * @Resource:
 */
public class OpenResult<T extends Serializable> implements Serializable {

    private static final Long serialVersionUID = 1L;

    private ResultStatus status;

    private String message;

    private T data;

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OpenResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
