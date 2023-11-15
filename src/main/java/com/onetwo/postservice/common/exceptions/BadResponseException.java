package com.onetwo.postservice.common.exceptions;

import lombok.Getter;

@Getter
public class BadResponseException extends RuntimeException {

    public BadResponseException(String message) {
        super(message);
    }
}
