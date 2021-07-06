package com.walmart.fulfillmentutiltrigger.CommonUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.walmart.fulfillmentutiltrigger.Constants.CommonConstants;

@Component
public class LoggerUtil {
    @Value("${log.level}")
    private String logLevel;

    public void info(String message, Throwable... throwables) {
        if (CommonConstants.LOG_LEVEL_INFO.equalsIgnoreCase(logLevel) || CommonConstants.LOG_LEVEL_DEBUG.equalsIgnoreCase(logLevel))
            print(message, throwables);
    }

    public void error(String message, Throwable... throwables) {
        if (CommonConstants.LOG_LEVEL_ERROR.equalsIgnoreCase(logLevel) || CommonConstants.LOG_LEVEL_DEBUG.equalsIgnoreCase(logLevel))
            print(message, throwables);
    }

    public void debug(String message, Throwable... throwables) {
        if (CommonConstants.LOG_LEVEL_DEBUG.equalsIgnoreCase(logLevel))
            print(message, throwables);
    }

    private void print(String message, Throwable... throwables) {
        System.out.println(message);
        if (null != throwables) {
            for (Throwable throwable : throwables)
                throwable.printStackTrace();
        }
    }
}
