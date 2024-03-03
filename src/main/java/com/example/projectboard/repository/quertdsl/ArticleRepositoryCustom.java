package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    /**
     * test 부작용을 생각하여 @Deprecated 처리
     */
    @Deprecated
    List<Object> findAllDistinctHashtags();

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}

