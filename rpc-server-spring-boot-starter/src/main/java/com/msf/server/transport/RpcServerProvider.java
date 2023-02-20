package com.msf.server.transport;


import com.msf.core.common.ServiceInfo;
import com.msf.core.common.ServiceUtil;
import com.msf.core.register.RegistryService;
import com.msf.server.annotation.RpcService;
import com.msf.server.config.RpcServerProperties;
import com.msf.server.store.LocalServerCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import java.net.InetAddress;

/**
 * @Classname RpcServerProvider
 * @Description
 * @Date 2021/7/5 11:38
 * @Created by wangchangjiu
 */
// BeanPostProcessor 对每个bean进行处理
@Slf4j
public class RpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private RegistryService registryService;

    private RpcServerProperties properties;

    private RpcServer rpcServer;

    public RpcServerProvider(RegistryService registryService, RpcServer rpcServer, RpcServerProperties properties) {
        this.registryService = registryService;
        this.properties = properties;
        this.rpcServer = rpcServer;
    }


    /**
     * 所有bean 实例化之后处理
     * 暴露服务注册到注册中心
     * 容器启动后开启netty服务处理请求
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 找到RpcService注解的bean
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            try {
                // 获取接口名称
                String serviceName = rpcService.interfaceType().getName();
                // 获得接口的版本号
                String version = rpcService.version();
                // 将暴露的服务缓存到本地，这里的bean是 HelloWorldServiceImpl
                LocalServerCache.store(ServiceUtil.serviceKey(serviceName, version), bean);

                // 服务发现的承载，设置信息
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(ServiceUtil.serviceKey(serviceName, version));
                serviceInfo.setPort(properties.getPort());
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setAppName(properties.getAppName());

                // 服务注册
                registryService.register(serviceInfo);
            } catch (Exception ex) {
                log.error("服务注册出错:{}", ex);
            }
        }
        return bean;
    }

    /**
     * 启动rpc服务 处理请求 任何在上下文容器之内的bean都可以实现run方法
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        // 开启一个线程开启NettyRpcServer.
        new Thread(() -> rpcServer.start(properties.getPort())).start();
        log.info(" rpc server :{} start, appName :{} , port :{}", rpcServer, properties.getAppName(), properties.getPort());
        // 开启一个线程关闭，程序正常退出, JVM关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 关闭之后把服务从ZK上清楚
                registryService.destroy();
            }catch (Exception ex){
                log.error("", ex);
            }
        }));
    }

}
