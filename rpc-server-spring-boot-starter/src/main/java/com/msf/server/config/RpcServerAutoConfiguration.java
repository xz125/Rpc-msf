package com.msf.server.config;

import com.msf.core.register.RegistryService;
import com.msf.core.register.ZookeeperRegistryService;
import com.msf.server.transport.NettyRpcServer;
import com.msf.server.transport.RpcServer;
import com.msf.server.transport.RpcServerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : msf
 * @date : 2023/2/18
 */

@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
@ConditionalOnProperty(
        prefix = "rpc.server",
        name = {"enabled"},
        havingValue = "true")
public class RpcServerAutoConfiguration {
    
    @Autowired
    private RpcServerProperties properties;

    /**
     * 服务的注册，传入zookeeper地址和端口号
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RegistryService registryService() {
        return new ZookeeperRegistryService(properties.getRegistryAddr());
    }

    /**
     * 开启服务
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    public RpcServer RpcServer() {
        return new NettyRpcServer();
    }

    /**
     * 开启服务注册，开启NettyRpc服务端
     * @param registryService
     * @param rpcServer
     * @param rpcServerProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RpcServerProvider.class)
    public RpcServerProvider rpcServerProvider(@Autowired RegistryService registryService,
                                        @Autowired RpcServer rpcServer,
                                        @Autowired RpcServerProperties rpcServerProperties){
        return new RpcServerProvider(registryService, rpcServer, rpcServerProperties);
    }
}
