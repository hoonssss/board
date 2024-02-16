package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        return from(article)
            .distinct()
            .select(article.hashtag)
            .where(article.hashtag.isNotNull())
            .fetch();
    }

}