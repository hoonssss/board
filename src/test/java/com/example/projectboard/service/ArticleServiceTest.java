package com.example.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleUpdateDto;
import com.example.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;

    /**
     * 검색
     * 각 게시글 페이지 이동
     * 페이지네이션
     */
    @Test
    @DisplayName("게시글 검색 시 게시글 리스트 반환")
    void givenSearchParameters_whenSearchingArticles_thenReturnArticleList(){
        //Given

        //When
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");

        //Then
        assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글 조회하면 게시글을 반환")
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        //Given

        //When
        ArticleDto articles = sut.searchArticle(1L);

        //Then
        assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다")
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        //Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        //When
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "JH","title","content","hashtag"));

        //Then
        then(articleRepository).should().save(any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID, 수정 정보를 입력하면 게시글을 수정한다")
    void givenArticleModifiedInfo_whenUpdateArticle_thenUpdateArticle(){
        //Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        //When
        sut.updateArticle(1L, ArticleUpdateDto.of("title","content","hashtag"));

        //Then
        then(articleRepository).should().save(any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID를 입력하면 게시글을 삭제한다")
    void givenArticleId_whenDeleteArticle_thenDeleteArticle() {
        //Given
        willDoNothing().given(articleRepository).delete(any(Article.class));

        //When
        sut.deleteArticle(1L);

        //Then
        then(articleRepository).should().delete(any(Article.class));
    }
}