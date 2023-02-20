package com.msf.core.serialization;

import lombok.Getter;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public enum SerializationTypeEnum {
    /**
     * 序列化类型
     */
    HESSIAN((byte) 0),
    JSON((byte) 1);
    @Getter
    private final byte type;

    SerializationTypeEnum(byte type) {
        this.type = type;
    }

    public static SerializationTypeEnum parseByName(String typeName) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(typeName)) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

    public static SerializationTypeEnum parseByType(byte type) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

}
