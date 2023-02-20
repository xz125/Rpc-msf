package com.msf.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author : msf
 * @date : 2023/2/18
 */

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface RpcService {
    /**
     *  暴露服务接口类型
     * @return
     */
    Class<?> interfaceType() default Object.class;

    /**
     *  服务版本
     * @return
     */
    String version() default "1.0";
}
