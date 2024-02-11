package com.example.projectboard.repository;

import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.repository.quertdsl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HashtagRepository extends JpaRepository<Hashtag, Long>,
    QuerydslPredicateExecutor<Hashtag>, HashtagRepositoryCustom {

}
