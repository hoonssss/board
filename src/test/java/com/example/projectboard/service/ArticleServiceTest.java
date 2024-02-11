package com.example.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.HashtagDto;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.repository.ArticleRepository;
import com.example.projectboard.repository.HashtagRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    /**
     * 검색 각 게시글 페이지 이동 페이지네이션
     */
    @Test
    @DisplayName("게시글 검색 시 게시글 페이지 반환")
    void givenSearchParameters_whenSearchingArticles_thenReturnArticleList() {
        //Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        //When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        //Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnEmptyPage() {
        //Given
        Pageable pageable = Pageable.ofSize(20);

        //When
        Page<ArticleDto> articleDtos = sut.searchArticlesViaHashtag(null,pageable);


        //Then
        assertThat(articleDtos).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 게시글 페이지를 반환한다")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnArticlesPage() {
        //Given
        String hashtagName = "java";
        Pageable pageable = Pageable.ofSize(20);
        Article article = createArticle(1L);
        given(articleRepository.findByHashtagNames(List.of(hashtagName),pageable)).willReturn(Page.empty(pageable));

        //When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtagName,pageable);


        //Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtagNames(List.of(hashtagName),pageable);
    }

    @Test
    @DisplayName("게시글 조회하면 게시글을 반환")
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Article article = createArticle(1L);
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        // When
        sut.getArticle(article.getId());

        // Then
        then(articleRepository).should().findById(article.getId());
    }

    @Test
    @DisplayName("게시글의 ID, 수정 정보를 입력하면 게시글을 수정한다")
    void givenArticleModifiedInfo_whenUpdateArticle_thenUpdateArticle() {
        //Given
        Article article = createArticle(1L);
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);

        //When
        sut.updateArticle(dto);

        //Then
        System.out.println(article);
        assertThat(article)
            .isNotNull();
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @Test
    @DisplayName("게시글의 ID를 입력하면 게시글을 삭제한다")
    void givenArticleId_whenDeleteArticle_thenDeleteArticle() {
        //Given
        long articleId = 1L;
        Article article = createArticle(articleId);

        //When
        sut.deleteArticle(article.getId());

        //Then
        then(articleRepository).should().deleteById(articleId);
    }

    @DisplayName("해시태그를 조회하면 유니크 해시태그 리스트를 반환한다.")
    @Test
    void givenNothing_whenCalling_thenReturnHashtags() {
        //Given
        Article article = createArticle(1L);
        List<String> expectedHashtags = List.of("#java","#spring","#boot");
        given(hashtagRepository.findAllHashtagNames()).willReturn(expectedHashtags);

        //When
        List<String> actualHashtags = sut.getHashtags();

        //Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(hashtagRepository).should().findAllHashtagNames();
    }

    private UserAccount createUserAccount() {
        return createUserAccount("uno");
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.of(
            userId,
            "password",
            "uno@email.com",
            "Uno",
            null
        );
    }

    private Article createArticle(Long id) {
        Article article = Article.of(
            createUserAccount(),
            "title",
            "content"
        );
        article.addHashtags(Set.of(
            createHashtag(1L, "java"),
            createHashtag(2L, "spring")
        ));
        ReflectionTestUtils.setField(article, "id", id);

        return article;
    }

    private Hashtag createHashtag(String hashtagName) {
        return createHashtag(1L, hashtagName);
    }

    private Hashtag createHashtag(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.of(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);

        return hashtag;
    }

    private HashtagDto createHashtagDto() {
        return HashtagDto.of("java");
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content");
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
            1L,
            createUserAccountDto(),
            title,
            content,
            null,
            LocalDateTime.now(),
            "jh",
            LocalDateTime.now(),
            "jh");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            "jh",
            "password",
            "jh@mail.com",
            "jh",
            "This is memo",
            LocalDateTime.now(),
            "jh",
            LocalDateTime.now(),
            "jh"
        );
    }

}