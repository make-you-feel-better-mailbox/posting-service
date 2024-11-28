package com.onetwo.postservice.adapter.out.grpc;

import com.onetwo.rpc.user.UserGrpc;
import com.onetwo.rpc.user.UserId;
import com.onetwo.rpc.user.UserInfo;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import onetwo.mailboxcommonconfig.common.exceptions.BadResponseException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserGrpcClient {

    @GrpcClient("user")
    private UserGrpc.UserBlockingStub userStub;

    public UserInfo getUserInfo(String userId) {
        try {
            UserId userIdOj = UserId.newBuilder().setUserId(userId).build();
            UserInfo userInfo = this.userStub.getUserInfo(userIdOj);

            log.info("grpc client get user nickname - request user id = {}, response user info = {}", userId, userInfo);

            return userInfo;
        } catch (StatusRuntimeException e) {
            log.error("StatusRuntimeException = " + e);
            throw new BadResponseException(e.getMessage());
        }
    }
}
