package com.walmart.fulfillmentutiltrigger.Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by p0k00df on 06/07/21
 **/
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper = JsonUtil.getInstance();

    private JsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper getInstance() {
        if (objectMapper != null) {
            return objectMapper;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(Object value) throws JsonProcessingException {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error serializing object to JSON={}", ex);
            throw ex;
        }
    }

    public static String toPrettyJson(Object value) throws JsonProcessingException {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error serializing object to JSON={}", ex);
            throw ex;
        }
    }

    public static <T> T toObject(String in, Class<T> cls) throws IOException {
        if (StringUtils.isEmpty(in)) {
            return null;
        }
        try {
            return objectMapper.readValue(in, cls);
        } catch (IOException ex) {
            LOGGER.error("Error deserializing JSON to OBject ={}", ex);
            throw ex;
        }
    }

    public static <T> T toObject(String in, TypeReference<T> reference) throws IOException {
        if (StringUtils.isEmpty(in)) {
            return null;
        }
        try {
            return objectMapper.readValue(in, reference);
        } catch (IOException ex) {
            LOGGER.error("Error deserializing JSON to OBject ={}", ex);
            throw ex;
        }
    }

    public static <T> T clone(T in, Class<T> clazz) throws IOException {
        String jsonSource = toJson(in);
        return toObject(jsonSource, clazz);
    }

    public static <T> T objectToObject(Object in, TypeReference<T> reference) {
        if (in == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(in, reference);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Error deserializing Object to OBject ={}", ex);
            throw ex;
        }
    }
}
