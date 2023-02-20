package com.msf.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class JsonSerialization implements RpcSerialization{

    private static final ObjectMapper MAPPER;

    static {

        // 序列化的时候序列对象的那些属性
        // JsonInclude.Include.ALWAYS      所有属性
        MAPPER = generateMapper(JsonInclude.Include.ALWAYS);
    }

    private static ObjectMapper generateMapper(JsonInclude.Include include) {
        ObjectMapper customMapper = new ObjectMapper();

        customMapper.setSerializationInclusion(include);
        // 反序列化时,遇到未知属性会不会报错
        // true - 遇到没有的属性就报错 false - 没有的属性不会管，不会报错
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }


    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return obj instanceof String ?
                ((String) obj).getBytes() :
                MAPPER.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return MAPPER.readValue(Arrays.toString(data), clz);
    }
}
