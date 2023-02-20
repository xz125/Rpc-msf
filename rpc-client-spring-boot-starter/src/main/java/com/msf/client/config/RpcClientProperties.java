package com.msf.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : msf
 * @date : 2023/2/18
 */


@Data
//@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientProperties {
    /**
     *  负载均衡
     */
    private String balance;

    /**
     *  序列化
     */
    private String serialization;

    /**
     *  服务发现地址
     */
    private String discoveryAddr = "192.168.3.4:2181";

    /**
     *  服务调用超时
     */
    private Integer timeout;
}
