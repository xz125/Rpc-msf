package com.msf.core.balancer;

import com.msf.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class RandomBalance implements LoadBalance{
    private static Random random = new Random();
    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        return services.get(random.nextInt(services.size()));
    }
}
