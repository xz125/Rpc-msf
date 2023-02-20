package com.msf.core.register;

import com.msf.core.common.ServiceInfo;

import java.io.IOException;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public interface RegistryService {

    void register(ServiceInfo serviceInfo) throws Exception;

    void unRegister(ServiceInfo serviceInfo) throws Exception;

    void destroy() throws IOException;
}
