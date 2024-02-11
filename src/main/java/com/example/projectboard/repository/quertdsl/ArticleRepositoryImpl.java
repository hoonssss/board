package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import com.example.projectboard.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom,HashtagRepositoryCustom{

    public ArticleRepositoryImpl() {
        super(Article.class);
    }


    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle qArticle = QArticle.article;

        return from(qArticle)
            .distinct()
            .select(qArticle.hashtags.any().hashtagName)
            .fetch();
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;

        JPQLQuery<Article> query = from(article)
            .innerJoin(article.hashtags, hashtag)
            .where(hashtag.hashtagName.in(hashtagNames));
        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(articles,pageable,query.fetchCount());
    }

    @Override
    public List<String> findAllHashtagNames() {
        return null;
    }
}
