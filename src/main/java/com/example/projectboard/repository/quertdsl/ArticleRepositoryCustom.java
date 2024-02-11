package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.Article;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    /**
     * @see HashtagRepositoryCustom#findAllHashtagNames()
     * @deprecated 해시태그 도메인을 새로 만들었으므로 이 코드는 더 이상 사용할 필요 없다.
     */

    List<String> findAllDistinctHashtags();
    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}