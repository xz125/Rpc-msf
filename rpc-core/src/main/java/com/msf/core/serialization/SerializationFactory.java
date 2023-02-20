package com.msf.core.serialization;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class SerializationFactory {
    public static RpcSerialization getRpcSerialization(SerializationTypeEnum typeEnum) {
        switch (typeEnum) {
            case HESSIAN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal");
        }
    }
}
