package com.msf.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : msf
 * @date : 2023/2/19
 */
@Data
public class RpcRequest implements Serializable {
    /**
     * 请求的服务名 + 版本
     */
    private String serviceName;
    /**
     * 请求调用的方法
     */
    private String method;

    /**
     *  参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] parameters;

}
