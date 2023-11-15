package com.onetwo.postservice.common.exceptions;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
