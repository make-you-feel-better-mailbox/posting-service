package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.application.port.out.dto.UserInfoResponse;

public interface ReadUserPort {
    UserInfoResponse getUserInfo(String userId);
}
