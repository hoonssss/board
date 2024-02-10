package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword,
        Pageable pageable) {
        if (searchType == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable)
                .map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable)
                .map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable)
                .map(ArticleDto::from);
            case NICKNAME ->
                articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtagNames(
                    Arrays.stream(searchKeyword.split(" ")).toList(),
                    pageable
                )
                .map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(long articleId) {
        return articleRepository.findById(articleId).map(ArticleWithCommentsDto::from)
            .orElseThrow(
                () -> new EntityNotFoundException("게시글이 없습니다 - articleId:" + articleId)
            );
    }

    public Article saveArticle(ArticleDto dto) {
        return articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            if (dto.title() != null) {
                article.setTitle(dto.title());
            }
            if (dto.content() != null) {
                article.setContent(dto.content());
            }
            article.addHashtag((Hashtag) dto.hashtagDtos());
        } catch (EntityNotFoundException e) {
            log.warn("update error {}", e.getMessage());
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
