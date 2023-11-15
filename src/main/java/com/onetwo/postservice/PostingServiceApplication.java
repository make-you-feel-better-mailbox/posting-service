package com.onetwo.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PostingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostingServiceApplication.class, args);
    }

}
