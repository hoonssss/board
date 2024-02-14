package com.example.projectboard.repository;

import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.repository.quertdsl.HashtagRepositoryCustom;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HashtagRepository extends
    JpaRepository<Hashtag, Long>,
    HashtagRepositoryCustom,
    QuerydslPredicateExecutor<Hashtag> {

    Optional<Hashtag> findByHashtagName(String hashtagName);
    List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}