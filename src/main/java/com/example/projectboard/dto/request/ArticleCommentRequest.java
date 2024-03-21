package com.example.projectboard.dto.request;

import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.UserAccountDto;
import java.time.LocalDateTime;

public record ArticleCommentRequest(
    Long articleId,
    Long parentCommentId,
    String content
) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return ArticleCommentRequest.of(articleId, null, content);
    }

    public static ArticleCommentRequest of(Long articleId, Long parentCommentId, String content) {
        return new ArticleCommentRequest(articleId, parentCommentId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
            articleId,
            userAccountDto,
            parentCommentId,
            content
        );
    }

}

