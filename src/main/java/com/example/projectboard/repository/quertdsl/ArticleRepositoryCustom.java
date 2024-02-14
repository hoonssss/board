package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    List<String> findAllDistinctHashtags();
//    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
