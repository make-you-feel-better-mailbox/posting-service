package com.onetwo.postservice.application.service.adapter;

import com.onetwo.postservice.adapter.out.grpc.UserGrpcClient;
import com.onetwo.postservice.application.port.out.ReadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcAdapter implements ReadUserPort {

    private final UserGrpcClient userGrpcClient;

    @Override
    public String getUserNickname(String userId) {
        return userGrpcClient.getUserNickname(userId);
    }
}
