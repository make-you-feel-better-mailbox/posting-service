package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.domain.Posting;

public interface RegisterPostingPort {
    Posting registerPosting(Posting newPosting);
}
