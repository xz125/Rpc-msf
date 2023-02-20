package com.msf.core.protocol;

import lombok.Getter;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public enum MsgStatus {
    /**
     * SUCCESS: 表示成功
     * FAIL: 表示失败
     */
    SUCCESS((byte) 0),
    FAIL((byte) 1);

    @Getter
    private byte code;

    MsgStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code) {
        return MsgStatus.SUCCESS.code == code;
    }
}
