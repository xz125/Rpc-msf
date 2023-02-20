package com.msf.core.register;

import com.msf.core.common.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;

/**
 * @author : msf
 * @date : 2023/2/19
 */

@Slf4j
public class ZookeeperRegistryService implements RegistryService {
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/demo_rpc";

    // 服务发开启
    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            client.start();
            // 设置了数据的序列化方式
            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(serializer)
                    .basePath(ZK_BASE_PATH)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            log.error("serviceDiscovery start error :{}", e);
        }
    }

    // 注册这个服务实例
    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName()) // 服务名称
                .address(serviceInfo.getAddress()) // 服务地址
                .port(serviceInfo.getPort())
                .payload(serviceInfo) // 服务的承载
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance
                .<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
