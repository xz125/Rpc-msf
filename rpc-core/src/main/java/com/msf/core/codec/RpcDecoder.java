package com.msf.core.codec;

import com.msf.core.common.RpcRequest;
import com.msf.core.common.RpcResponse;
import com.msf.core.protocol.MessageHeader;
import com.msf.core.protocol.MessageProtocol;
import com.msf.core.protocol.MsgType;
import com.msf.core.protocol.ProtocolConstants;
import com.msf.core.serialization.RpcSerialization;
import com.msf.core.serialization.SerializationFactory;
import com.msf.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author : msf
 * @date : 2023/2/19
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * +---------------------------------------------------------------+
     * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte|
     * +---------------------------------------------------------------+
     * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
     * +---------------------------------------------------------------+
     * |                   数据内容 （长度不定）                         |
     * +---------------------------------------------------------------+
     * <p>
     * decode 这个方法会被循环调用
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            // 可读的数据小于请求头总的大小 直接丢弃
            return;
        }
        // 标记 ByteBuf 读指针位置
        in.markReaderIndex();
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }
        byte version = in.readByte();
        byte serializeType = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        CharSequence requestId = in.readCharSequence(ProtocolConstants.REQ_LEN, Charset.forName("UTF-8"));
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            // 可读的数据长度小于 请求体长度 直接丢弃并重置 读指针位置
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        // 如果消息类型不对直接返回
        MsgType msgTypeEnum = MsgType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }


        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializeType);
        header.setStatus(status);
        header.setRequestId(String.valueOf(requestId));
        header.setMsgType(msgType);
        header.setMsgLen(dataLength);
        // 得到序列化的类型
        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization
                (SerializationTypeEnum.parseByType(serializeType));

        // 解析消息的类型
        switch (msgTypeEnum) {
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    MessageProtocol<RpcRequest> protocol = new MessageProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    MessageProtocol<RpcResponse> protocol = new MessageProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
        }
    }
}
