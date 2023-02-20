package com.msf.client.config;

import com.msf.client.professor.RpcClientProcessor;
import com.msf.client.proxy.ClientStubProxyFactory;
import com.msf.core.balancer.FullRoundBalance;
import com.msf.core.balancer.LoadBalance;
import com.msf.core.balancer.RandomBalance;
import com.msf.core.discovery.DiscoveryService;
import com.msf.core.discovery.ZookeeperDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @author : msf
 * @date : 2023/2/18
 */


@Configuration
//@EnableConfigurationProperties(RpcClientProperties.class)
@ConditionalOnProperty(
        prefix = "rpc.client",
        name = {"enabled"},
        havingValue = "true")
public class RpcClientAutoConfiguration {

    /*@Autowired
    private RpcClientProperties rpcClientProperties;*/

    /**
     * 获得 RpcClientProperties
     * @param environment
     * @return
     */
    @Bean
    public RpcClientProperties rpcClientProperties(Environment environment){
        BindResult<RpcClientProperties> result = Binder.
                get(environment).
                bind("rpc.client", RpcClientProperties.class);
        return result.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientStubProxyFactory clientStubProxyFactory() {
        return new ClientStubProxyFactory();
    }

    @Primary
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "rpc.client",
            name = "balance",
            havingValue = "randomBalance",
            matchIfMissing = true)
    public LoadBalance randomBalance() {
        return new RandomBalance();
    }

    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "rpc.client",
            name = "balance",
            havingValue = "fullRoundBalance")
    public LoadBalance loadBalance() {
        return new FullRoundBalance();
    }

    // 只有加载了 RpcClientProperties 和 LoadBalance 才会加载
    // 开启发现
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
    public DiscoveryService discoveryService(@Autowired RpcClientProperties properties,
                                             @Autowired LoadBalance loadBalance) {
        return new ZookeeperDiscoveryService(properties.getDiscoveryAddr(), loadBalance);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientProcessor rpcClientProcessor(@Autowired ClientStubProxyFactory clientStubProxyFactory,
                                                 @Autowired DiscoveryService discoveryService,
                                                 @Autowired RpcClientProperties properties) {
        return new RpcClientProcessor(clientStubProxyFactory, discoveryService, properties);
    }

}
