package com.onetwo.postservice.application.port.out.dto;

public record UserInfoResponse(
        String userNickname,
        String userProfileImageEndPoint
) {
}
