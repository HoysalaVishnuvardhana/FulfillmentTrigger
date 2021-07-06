package com.walmart.fulfillmentutiltrigger.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class ConverterUtil {

    private ObjectMapper objectMapper;

    public <T>  T jsonStringToObject(String jsonString, Class<T> tClass) throws Exception {
        return objectMapper.readValue(jsonString, tClass);
    }

    public String objectToJsonString(Object object) throws Exception {
        if(null==object)
            return null;
        else
            return objectMapper.writeValueAsString(object);
    }
}
