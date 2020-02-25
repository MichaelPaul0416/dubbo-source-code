package org.apache.dubbo.demo.provider;

import org.apache.dubbo.demo.RemoteService;
import org.apache.dubbo.model.OpenParam;
import org.apache.dubbo.model.OpenResult;
import org.apache.dubbo.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wangqiang20995
 * @Date:2019/5/15
 * @Description:
 * @Resource:
 */
public class RemoteServiceImpl implements RemoteService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String currentDateTime;

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    @Override
    public OpenResult<String> callRemote(OpenParam param) {
        logger.info("请求参数[{}]",param);
        OpenResult<String> result = new OpenResult<>();
        result.setStatus(ResultStatus.SUCCESS);
        result.setMessage("call success");
        result.setData(param.getRequestParam());
        return result;
    }
}
