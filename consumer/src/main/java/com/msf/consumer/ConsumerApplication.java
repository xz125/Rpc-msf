package com.msf.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author : msf
 * @date : 2023/2/18
 */
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ConsumerApplication.class, args);

        // 测试starter
        /*RpcClientAutoConfiguration bean = context.getBean(RpcClientAutoConfiguration.class);
        System.out.println("bean = " + bean);
        RpcClientProperties bean1 = context.getBean(RpcClientProperties.class);
        System.out.println("bean1 = " + bean1);*/
    }
}
