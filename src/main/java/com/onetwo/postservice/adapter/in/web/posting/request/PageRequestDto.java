package com.onetwo.postservice.adapter.in.web.posting.request;

public record PageRequestDto(Integer pageNumber,
                             Integer pageSize,
                             String sort) {
}
