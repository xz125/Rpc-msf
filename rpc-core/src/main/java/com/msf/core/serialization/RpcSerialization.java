package com.msf.core.serialization;

import java.io.IOException;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
