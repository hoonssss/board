package com.example.projectboard.dto.projection;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.UserAccount;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name ="withUserAccount", types = Article.class)
public interface ArticleProjection {
    Long getId();
    UserAccount getUserAccount();
    String getTitle();
    String getContent();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}