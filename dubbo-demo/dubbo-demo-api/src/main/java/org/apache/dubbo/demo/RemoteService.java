package org.apache.dubbo.demo;

import org.apache.dubbo.model.OpenParam;
import org.apache.dubbo.model.OpenResult;

/**
 * @Author: wangqiang20995
 * @Date:2019/5/15
 * @Description:
 * @Resource:
 */
public interface RemoteService {
    OpenResult<String> callRemote(OpenParam param);
}
