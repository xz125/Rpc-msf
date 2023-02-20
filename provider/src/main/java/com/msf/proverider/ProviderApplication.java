package com.msf.proverider;

import com.msf.server.config.RpcServerAutoConfiguration;
import com.msf.server.config.RpcServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : msf
 * @date : 2023/2/18
 */
@SpringBootApplication
@ComponentScan("com.msf")
public class ProviderApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ProviderApplication.class, args);

        // 测试starter服务
        /*RpcServerAutoConfiguration bean = context.getBean(RpcServerAutoConfiguration.class);
        System.out.println("bean = " + bean);
        RpcServerProperties bean1 = context.getBean(RpcServerProperties.class);
        System.out.println("bean1 = " + bean1);*/

    }
}
