package com.katzenyasax.mall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@MapperScan(value = "com.katzenyasax.mall.ware.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.katzenyasax.mall.ware.feign")
@SpringBootApplication
public class MallWare {

    public static void main(String[] args) {
        SpringApplication.run(MallWare.class, args);
    }

}
