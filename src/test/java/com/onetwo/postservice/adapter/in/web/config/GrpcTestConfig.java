package com.onetwo.postservice.adapter.in.web.config;

import com.onetwo.postservice.adapter.out.grpc.UserGrpcClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class GrpcTestConfig {

    @Bean
    @Primary
    public UserGrpcClient userGrpcClient() {
        return new UserGrpcClient() {

            @Override
            public String getUserNickname(String userId) {
                return "test";
            }
        };
    }
}
