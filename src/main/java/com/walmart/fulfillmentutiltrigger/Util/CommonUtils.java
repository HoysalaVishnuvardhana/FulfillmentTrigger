package com.walmart.fulfillmentutiltrigger.Util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.fulfillmentutiltrigger.CommonUtil.LoggerUtil;

public class CommonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private LoggerUtil loggerUtil;

    public static String ReadFromFile(String path) throws IOException {
        ClassLoader classLoader = CommonUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static <T> T toObject(String in, Class<T> cls) throws IOException {
        if (StringUtils.isEmpty(in)) {
            return null;
        }
        try {
            return objectMapper.readValue(in, cls);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
