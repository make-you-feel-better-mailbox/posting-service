package com.onetwo.postservice.adapter.out.persistence.repository.posting;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.common.GlobalStatus;
import com.onetwo.postservice.common.utils.SliceUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.onetwo.postservice.adapter.out.persistence.entity.QPostingEntity.postingEntity;

public class QPostingRepositoryImpl extends QuerydslRepositorySupport implements QPostingRepository {

    private final JPAQueryFactory factory;

    public QPostingRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(PostingEntity.class);
        this.factory = jpaQueryFactory;
    }

    @Override
    public List<PostingEntity> sliceByUserId(String userId, Pageable pageable) {
        return factory.select(postingEntity)
                .from(postingEntity)
                .where(postingEntity.userId.eq(userId),
                        postingEntity.state.eq(GlobalStatus.PERSISTENCE_NOT_DELETED))
                .limit(SliceUtil.getSliceLimit(pageable.getPageSize()))
                .offset(pageable.getOffset())
                .orderBy(postingEntity.id.desc())
                .fetch();
    }
}
