package com.msf.core.balancer;

import com.msf.core.common.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : msf
 * @date : 2023/2/18
 */
public class FullRoundBalance implements LoadBalance {
    private static Logger logger = LoggerFactory.getLogger(FullRoundBalance.class);
    private int index;

    @Override
    public synchronized ServiceInfo chooseOne(List<ServiceInfo> services) {
        // 加锁防止多线程情况下，index超出services.size()
        if (index >= services.size()) {
            index = 0;
        }
        return services.get(index++);
    }
}
