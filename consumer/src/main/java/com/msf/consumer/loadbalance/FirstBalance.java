package com.msf.consumer.loadbalance;

import com.msf.core.balancer.LoadBalance;
import com.msf.core.common.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author : msf
 * @date : 2023/2/18
 */

@Slf4j
public class FirstBalance implements LoadBalance {


    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        log.info("---------FirstLoadBalance-----------------");
        return services.get(0);
    }
}
