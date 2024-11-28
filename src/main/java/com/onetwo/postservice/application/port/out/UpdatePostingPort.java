package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.domain.Posting;

public interface UpdatePostingPort {
    void updatePosting(Posting posting);
}
