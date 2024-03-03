package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import com.example.projectboard.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<Object> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        return Collections.singletonList(from(article)
            .distinct()
            .select(article.hashtags.any())
            .fetch());
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QArticle article = QArticle.article;

        JPQLQuery<Article> query = from(article)
            .innerJoin(article.hashtags, hashtag)
            .where(hashtag.hashtagName.in(hashtagNames));
        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }

}