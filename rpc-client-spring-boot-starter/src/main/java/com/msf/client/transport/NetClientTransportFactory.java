package com.msf.client.transport;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class NetClientTransportFactory {

    public static NetClientTransport getNetClientTransport() {
        return new NettyNetClientTransport();
    }
}
