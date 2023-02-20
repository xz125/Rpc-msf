package com.msf.core.protocol;

import lombok.Getter;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public enum MsgType {
    /**
     * REQUEST:请求
     * RESPONSE响应
     */
    REQUEST((byte) 1),
    RESPONSE((byte) 2);

    @Getter
    private final byte type;

    MsgType(byte type) {
        this.type = type;
    }

    public static MsgType findByType(byte type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }
}
