package com.ohgiraffers.team3backendadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.ohgiraffers.team3backendadmin.infrastructure.client")
@SpringBootApplication
public class Team3BackendAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(Team3BackendAdminApplication.class, args);
    }

}
