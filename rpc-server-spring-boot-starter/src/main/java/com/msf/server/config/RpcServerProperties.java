package com.msf.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : msf
 * @date : 2023/2/18
 */

@Data
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServerProperties {

    /**
     *  服务启动端口
     */
    private Integer port = 8090;

    /**
     *  服务名称
     */
    private String appName;

    /**
     *  注册中心地址
     */
    private String registryAddr = "192.168.3.4:2181";
}
