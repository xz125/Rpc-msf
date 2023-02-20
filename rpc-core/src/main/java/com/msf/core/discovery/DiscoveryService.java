package com.msf.core.discovery;

import com.msf.core.common.ServiceInfo;

/**
 * @author : msf
 * @date : 2023/2/18
 */
public interface DiscoveryService {
    /**
     *  发现
     * @param serviceName
     * @return
     * @throws Exception
     */
    ServiceInfo discovery(String serviceName) throws Exception;
}
