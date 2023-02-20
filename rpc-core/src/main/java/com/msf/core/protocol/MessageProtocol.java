package com.msf.core.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : msf
 * @date : 2023/2/19
 */
@Data
public class MessageProtocol<T> implements Serializable {

     /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 32byte     |      数据长度 4byte     |
    |                         数据                                  |
    +---------------------------------------------------------------+
     */

    /**
     *  消息头
     */
    private MessageHeader header;

    /**
     *  消息体
     */
    private T body;
}
