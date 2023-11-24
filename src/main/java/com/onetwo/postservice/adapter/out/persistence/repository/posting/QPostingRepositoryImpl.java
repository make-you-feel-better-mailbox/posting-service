package com.onetwo.postservice.adapter.out.persistence.repository.posting;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.common.GlobalStatus;
import com.onetwo.postservice.common.utils.QueryDslUtil;
import com.onetwo.postservice.common.utils.SliceUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<PostingEntity> sliceByCommand(PostingFilterCommand postingFilterCommand) {
        return factory.select(postingEntity)
                .from(postingEntity)
                .where(filterCondition(postingFilterCommand),
                        postingEntity.state.eq(GlobalStatus.PERSISTENCE_NOT_DELETED))
                .limit(SliceUtil.getSliceLimit(postingFilterCommand.getPageable().getPageSize()))
                .offset(postingFilterCommand.getPageable().getOffset())
                .orderBy(postingEntity.id.desc())
                .fetch();
    }

    private Predicate filterCondition(PostingFilterCommand postingFilterCommand) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QueryDslUtil.ifConditionExistAddEqualPredicate(postingFilterCommand.getUserId(), postingEntity.userId, booleanBuilder);
        QueryDslUtil.ifConditionExistAddLikePredicate(postingFilterCommand.getContent(), postingEntity.content, booleanBuilder);
        QueryDslUtil.ifConditionExistAddGoePredicate(postingFilterCommand.getFilterStartDate(), postingEntity.createdAt, booleanBuilder);
        QueryDslUtil.ifConditionExistAddLoePredicate(postingFilterCommand.getFilterEndDate(), postingEntity.createdAt, booleanBuilder);

        return booleanBuilder;
    }
}
