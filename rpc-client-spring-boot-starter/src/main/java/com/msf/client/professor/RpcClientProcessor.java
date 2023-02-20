package com.msf.client.professor;

import com.msf.client.annotation.RpcAutowired;
import com.msf.client.config.RpcClientProperties;
import com.msf.client.proxy.ClientStubProxyFactory;
import com.msf.core.discovery.DiscoveryService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @Classname RpcClientProcessor
 * @Description bean 后置处理器 获取所有bean
 * 判断bean字段是否被 {@link com.msf.client.annotation.RpcAutowired } 注解修饰
 * 动态修改被修饰字段的值为代理对象 {@link ClientStubProxyFactory}
 * @author : msf
 * @date : 2023/2/19
 */
public class RpcClientProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ClientStubProxyFactory clientStubProxyFactory;

    private DiscoveryService discoveryService;

    private RpcClientProperties properties;

    private ApplicationContext applicationContext;

    public RpcClientProcessor(ClientStubProxyFactory clientStubProxyFactory, DiscoveryService discoveryService, RpcClientProperties properties) {
        this.clientStubProxyFactory = clientStubProxyFactory;
        this.discoveryService = discoveryService;
        this.properties = properties;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                // 获取类对象
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
                //对目标类中的所有字段调用给定的回调，向上进入类层次结构以获取所有声明的字段
                ReflectionUtils.doWithFields(clazz, field -> {
                    RpcAutowired rpcAutowired = AnnotationUtils.getAnnotation(field, RpcAutowired.class);
                    if (rpcAutowired != null) {
                        Object bean = applicationContext.getBean(clazz);
                        field.setAccessible(true);
                        // 修改为代理对象
                        ReflectionUtils.setField(
                                field,
                                bean,
                                clientStubProxyFactory.getProxy(
                                        field.getType(),
                                        rpcAutowired.version(),
                                        discoveryService,
                                        properties));
                    }
                });
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
