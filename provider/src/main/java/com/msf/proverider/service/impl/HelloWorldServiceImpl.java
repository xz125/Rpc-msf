package com.msf.proverider.service.impl;

import com.msf.api.HelloWorldService;
import com.msf.server.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : msf
 * @date : 2023/2/18
 */
@Slf4j
@RpcService(interfaceType = HelloWorldService.class, version = "1.0")
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHello(String name) {
        log.info("您好：" + name + ", rpc 调用成功");
        return String.format("您好：%s, rpc 调用成功", name);
    }
}
