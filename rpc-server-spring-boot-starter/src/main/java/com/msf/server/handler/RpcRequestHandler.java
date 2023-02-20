package com.msf.server.handler;

import com.msf.core.common.RpcRequest;
import com.msf.core.common.RpcResponse;
import com.msf.core.protocol.MessageHeader;
import com.msf.core.protocol.MessageProtocol;
import com.msf.core.protocol.MsgStatus;
import com.msf.core.protocol.MsgType;
import com.msf.server.store.LocalServerCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : msf
 * @date : 2023/2/19
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {

    /**
     *
     */
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000),
            Executors.defaultThreadFactory()
    );

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol) throws Exception {
        // 多线程处理每个请求
        threadPoolExecutor.submit(() -> {
            MessageProtocol<RpcResponse> resProtocol = new MessageProtocol<>();
            RpcResponse response = new RpcResponse();
            MessageHeader header = rpcRequestMessageProtocol.getHeader();
            // 设置头部消息类型为响应
            header.setMsgType(MsgType.RESPONSE.getType());
            try {
                // 对收到的数据进行处理
                Object result = handle(rpcRequestMessageProtocol.getBody());
                response.setData(result);
                header.setStatus(MsgStatus.SUCCESS.getCode());
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable throwable) {
                header.setStatus(MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }
            // 把数据写回去
            channelHandlerContext.writeAndFlush(resProtocol);
        });
    }

    /**
     * 反射调用获取数据
     *
     * @param request
     * @return
     */
    private Object handle(RpcRequest request) {
        try {
            Object bean = LocalServerCache.get(request.getServiceName());
            if (bean == null) {
                throw new RuntimeException(String.format("service not exist: %s !", request.getServiceName()));
            }
            // 反射调用
            Method method = bean.getClass().getMethod(request.getMethod(), request.getParameterTypes());
            return method.invoke(bean, request.getParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
