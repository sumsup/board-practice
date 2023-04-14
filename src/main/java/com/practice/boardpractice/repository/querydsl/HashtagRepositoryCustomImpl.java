package com.practice.boardpractice.repository.querydsl;

import com.practice.boardpractice.domain.Hashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {
    public HashtagRepositoryCustomImpl() {
        super(Hashtag.class);
    }

    @Override
    public List<String> findAllHashtagNames() {
        return null;
    }
}
