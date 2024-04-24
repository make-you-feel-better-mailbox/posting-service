package com.onetwo.postservice;

import com.onetwo.postservice.common.GlobalStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PostingServiceApplication {

    public static void main(String[] args) {
        String active = System.getProperty(GlobalStatus.SPRING_PROFILES_ACTIVE);
        if (active == null) {
            System.setProperty(GlobalStatus.SPRING_PROFILES_ACTIVE, GlobalStatus.SPRING_PROFILES_ACTIVE_DEFAULT);
        }

        System.setProperty(
                GlobalStatus.SPRING_PROFILES_ACTIVE,
                System.getProperty(GlobalStatus.SPRING_PROFILES_ACTIVE, GlobalStatus.SPRING_PROFILES_ACTIVE_DEFAULT)
        );
        SpringApplication.run(PostingServiceApplication.class, args);
    }

}
