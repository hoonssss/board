package com.example.projectboard.dto.response;

import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.dto.HashtagDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
    Long id,
    String title,
    String content,
    Set<String> hashtags,
    LocalDateTime createdAt,
    String email,
    String nickname,
    String userId,
    Set<ArticleCommentResponse> articleCommentsResponse
) {

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, userId, articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
            dto.id(),
            dto.title(),
            dto.content(),
            dto.hashTagDtos().stream()
                .map(HashtagDto::hashtagName)
                .collect(Collectors.toUnmodifiableSet()),
            dto.createdAt(),
            dto.userAccountDto().email(),
            nickname,
            dto.userAccountDto().userId(),
//            dto.articleCommentDtos().stream()
//                .map(ArticleCommentResponse::from)
//                .collect(Collectors.toCollection(LinkedHashSet::new))
            organizeChildComments(dto.articleCommentDtos())
        );
    }

    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos){
        Map<Long,ArticleCommentResponse> map = dtos.stream()
            .map(ArticleCommentResponse::from)
            .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

        map.values().stream()
            .filter(ArticleCommentResponse::hasParentComment)
            .forEach(comment -> {
                ArticleCommentResponse parentComment = map.get(comment.parentCommentId());
                parentComment.childComments().add(comment);
            });

        return map.values().stream()
            .filter(comment -> !comment.hasParentComment()) //자식 comment 확인
            .collect(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ArticleCommentResponse::createdAt)
                    .reversed() //내림차순
                    .thenComparingLong(ArticleCommentResponse::id)))
            ); //정렬
    }

}
