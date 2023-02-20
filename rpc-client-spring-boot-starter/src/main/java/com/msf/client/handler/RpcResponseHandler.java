package com.msf.client.handler;

import com.msf.client.cache.LocalRpcResponseCache;
import com.msf.core.common.RpcResponse;
import com.msf.core.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 数据响应处理器
 * @author : msf
 * @date : 2023/2/19
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol<RpcResponse> msg) throws Exception {
        String requestId = msg.getHeader().getRequestId();
        // 收到响应 设置响应数据
        LocalRpcResponseCache.fillResponse(requestId,msg);
    }
}
