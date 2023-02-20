package com.msf.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : msf
 * @date : 2023/2/19
 */

@Data
public class RpcResponse implements Serializable {
    private Object data;
    private String message;
}
