package com.msf.client.transport;

import com.msf.core.common.RpcResponse;
import com.msf.core.protocol.MessageProtocol;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public interface NetClientTransport {
    /**
     *  发送数据
     * @param metadata
     * @return
     * @throws Exception
     */
    MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception;
}
