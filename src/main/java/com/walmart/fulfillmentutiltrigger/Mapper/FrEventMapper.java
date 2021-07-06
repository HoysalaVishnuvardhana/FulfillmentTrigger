package com.walmart.fulfillmentutiltrigger.Mapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.fulfillmentutiltrigger.Util.CommonUtils;

@Component
public class FrEventMapper {
    public Map<String, Object> getFrData() throws IOException {
        String path = "FrEventSams.json";
        Map<String, Object> map = new HashMap<>();
        String message = CommonUtils.ReadFromFile(path);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> frMap = mapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        return frMap;
    }
}
