package com.example.projectboard.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.projectboard.config.JpaConfig;
import com.example.projectboard.domain.Article;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

//@ActiveProfiles("testdb")
@DisplayName("JPA 연결 TEST")
@Import(JpaConfig.class)
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    JpaRepositoryTest(
        @Autowired ArticleRepository articleRepository,
        @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @BeforeEach
    void BeforeEach() {
        Article article = new Article("test", "test", "setUpHashtag");
        articleRepository.save(article);
    }

    @DisplayName("조회")
    @Order(1)
    @Test
    void givenTestData_whenSelecting_thenWorkFine() {
        //given

        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles)
            .isNotNull()
            .hasSize(1);
    }

    @DisplayName("삽입")
    @Order(2)
    @Test
    void givenTestData_whenInserting_thenWorkFine() {
        //given
        long count = articleRepository.count();
        Article article = new Article("title", "content", "#spring");

        //when
        articleRepository.save(article);

        //then
        assertThat(articleRepository.count()).isEqualTo(count + 1);
    }

    @DisplayName("업데이트")
    @Order(3)
    @Test
    void givenTestData_whenUpdating_thenWorkFine() {
        //Given
        Article findArticle = articleRepository.findById(4L).orElseThrow();
        String updateHashtag = "#springboot";
        findArticle.setHashtag(updateHashtag);

        //When
        Article saveArticle = articleRepository.saveAndFlush(findArticle);//test update query

        //Then
        assertThat(saveArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
    }

    @DisplayName("삭제")
    @Order(4)
    @Test
    void givenTestData_whenDeleting_thenWorkFine() {
        //Given
        Article article = articleRepository.findById(5L).orElseThrow();
        long count = articleRepository.count();
        long comment = articleCommentRepository.count();
        int deleteComment = article.getArticleComments().size();

        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(count - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(comment - deleteComment);
    }
}