package com.msf.consumer.controller;

import com.msf.api.HelloWorldService;
import com.msf.client.annotation.RpcAutowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author : msf
 * @date : 2023/2/18
 */
@Controller
public class HelloWorldController {

    @RpcAutowired
    private HelloWorldService helloWorldService;

    @GetMapping("/hello/world")
    public ResponseEntity<String> pullServiceInfo(@RequestParam("name") String name){
        return  ResponseEntity.ok(helloWorldService.sayHello(name));
    }
}
