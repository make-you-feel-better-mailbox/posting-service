package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.domain.Posting;

import java.util.Optional;

public interface ReadPostingPort {
    Optional<Posting> findById(Long postingId);
}
