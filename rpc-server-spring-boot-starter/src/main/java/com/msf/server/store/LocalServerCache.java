package com.msf.server.store;


import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LocalServerCache
 * @Description 将暴露的服务缓存到本地
 * 在处理 RPC 请求时可以直接通过 cache 拿到对应的服务进行调用。避免反射实例化服务开销
 * @Date 2021/7/5 11:44
 * @Created by wangchangjiu
 */
public final class LocalServerCache {

    private static final Map<String, Object> serverCacheMap = new HashMap<>();

    // 将一个key-value放入hashmap中，如果map中存在旧值，依据你的代码进行替换。这次采用新值代替旧值。
    public static void store(String serverName, Object server) {
        serverCacheMap.merge(serverName, server, (Object oldObj, Object newObj) -> newObj);
    }

    public static Object get(String serverName) {
        return serverCacheMap.get(serverName);
    }

    public static Map<String, Object> getAll(){
        return null;
    }
}
