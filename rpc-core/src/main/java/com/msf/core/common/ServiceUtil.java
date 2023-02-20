package com.msf.core.common;

/**
 * @author : msf
 * @date : 2023/2/19
 */
public class ServiceUtil {

    /**
     *
     * @param serviceName
     * @param version
     * @return
     */
    public static String serviceKey(String serviceName, String version) {
        return String.join("-", serviceName, version);
    }
}
