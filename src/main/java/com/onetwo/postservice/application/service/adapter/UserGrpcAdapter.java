package com.onetwo.postservice.application.service.adapter;

import com.onetwo.postservice.adapter.out.grpc.UserGrpcClient;
import com.onetwo.postservice.application.port.out.ReadUserPort;
import com.onetwo.postservice.application.port.out.dto.UserInfoResponse;
import com.onetwo.rpc.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcAdapter implements ReadUserPort {

    private final UserGrpcClient userGrpcClient;

    @Override
    public UserInfoResponse getUserInfo(String userId) {
        UserInfo userInfo = userGrpcClient.getUserInfo(userId);

        return new UserInfoResponse(userInfo.getUserNickname(), userInfo.getProfileImageEndPoint());
    }
}
