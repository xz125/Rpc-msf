package com.msf.core.balancer;

import com.msf.core.common.ServiceInfo;

import java.util.List;

/**
 * @author : msf
 * @date : 2023/2/18
 */
public interface LoadBalance {

    /**
     *
     * @param services
     * @return
     */
    ServiceInfo chooseOne(List<ServiceInfo> services);
}
