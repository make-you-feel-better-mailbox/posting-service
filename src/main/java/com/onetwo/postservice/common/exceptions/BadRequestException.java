package com.onetwo.postservice.common.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
