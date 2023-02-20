package com.msf.client.transport;

import com.msf.core.common.RpcRequest;
import com.msf.core.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

/**
 * @author : msf
 * @date : 2023/2/19
 */

@Data
@Builder
public class RequestMetadata {
    /**
     *  协议
     */
    private MessageProtocol<RpcRequest> protocol;

    /**
     *  地址
     */
    private String address;

    /**
     *  端口
     */
    private Integer port;

    /**
     *  服务调用超时
     */
    private Integer timeout;
}
