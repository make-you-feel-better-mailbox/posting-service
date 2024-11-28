package com.onetwo.postservice.adapter.out.persistence.repository.posting;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<PostingEntity, Long>, QPostingRepository {
}
