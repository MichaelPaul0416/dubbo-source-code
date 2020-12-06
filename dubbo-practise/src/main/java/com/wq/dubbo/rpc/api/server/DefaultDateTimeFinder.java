package com.wq.dubbo.rpc.api.server;

import com.wq.dubbo.rpc.api.DateTimeFinder;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateTimeFinder implements DateTimeFinder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDateTimeFinder.class);

    private static final ThreadLocal<DateFormat> safeFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat());

    @Override
    public String currentTime(String format) {
        String f = StringUtils.isBlank(format) ? "yyyyMMddHHmmss" : format;
        logger.info("format:{}", f);
        DateFormat dateFormat = safeFormatter.get();
        return dateFormat.format(new Date());
    }
}
