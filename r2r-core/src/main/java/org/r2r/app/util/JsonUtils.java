package org.r2r.app.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // to prevent exception when encountering unknown property:
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> String toJson(T data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("toJson failed, source data: {}", data.toString(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> tClass) {
        if (tClass.equals(String.class)) {
            return (T) json;
        }
        if (json == null || "".equals(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            log.error("fromJson failed, source data: {}", json, e);
        }
        return null;
    }

    public static <L, P> List<P> fromJsonToList(String json, Class<L> listClass, Class<P> pojoClass) {
        if (json == null || "".equals(json)) {
            return Collections.emptyList();
        }
        try {
            TypeFactory typeFactory = MAPPER.getTypeFactory();
            JavaType type = typeFactory.constructParametricType(listClass, pojoClass);
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("fromJsonToList failed, source data: {}", json, e);
            return Collections.emptyList();
        }
    }

    public static <M, K, V> Map<K, V> fromJsonToMap(String json, Class<M> mapClass, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || "".equals(json)) {
            return Collections.emptyMap();
        }
        try {
            TypeFactory typeFactory = MAPPER.getTypeFactory();
            JavaType type = typeFactory.constructParametricType(mapClass, keyClass, valueClass);
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("fromJsonToMap failed, source data: {}", json, e);
            return Collections.emptyMap();
        }
    }

}
