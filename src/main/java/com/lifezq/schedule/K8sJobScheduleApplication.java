package com.lifezq.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class K8sJobScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sJobScheduleApplication.class, args);
    }
}
