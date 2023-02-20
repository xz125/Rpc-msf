package com.msf.client.proxy;

import com.msf.client.config.RpcClientProperties;
import com.msf.core.discovery.DiscoveryService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class ClientStubProxyFactory {


    private Map<Class<?>, Object> objectCache = new HashMap<>();

    /**
     * computeIfAbsent判断一个map中是否存在这个key，如果存在则处理value的数据，如果不存在，则创建一个满足value要求的数据结构放到val
     *
     * @param clazz            接口
     * @param version          服务版本
     * @param discoveryService
     * @param properties
     * @param <T>
     * @return 代理对象
     */
    public <T> T getProxy(Class<T> clazz, String version, DiscoveryService discoveryService, RpcClientProperties properties) {
        return (T) objectCache.computeIfAbsent(clazz, clz ->
                Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ClientStubInvocationHandler(discoveryService, properties, clz, version))
        );
    }
}
