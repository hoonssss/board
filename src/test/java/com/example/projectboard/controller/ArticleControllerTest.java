package com.example.projectboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.projectboard.config.SecurityConfig;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.dto.HashtagDto;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.service.ArticleService;
import com.example.projectboard.service.PaginationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("view 컨트롤러")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean
    ArticleService articleService;

    @MockBean
    PaginationService paginationService;

    @Autowired
    public ArticleControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(
            Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        mvc.perform(MockMvcRequestBuilders.get("/articles"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(MockMvcResultMatchers.model().attributeExists("articles")) //articles date가 있는지
            .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers")); //articles date가 있는지

        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 -> 검색어와 함께 호출")
    @Test
    void givenSearchKeyword_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType),eq(searchValue),any(Pageable.class))).willReturn(
            Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        mvc.perform(MockMvcRequestBuilders
                .get("/articles")
                .queryParam("searchType",searchType.name())
                .queryParam("searchValue",searchValue)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(MockMvcResultMatchers.model().attributeExists("articles")) //articles date가 있는지
            .andExpect(MockMvcResultMatchers.model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(searchType),eq(searchValue),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 게시글 단일 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());


        //When Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(MockMvcResultMatchers.model().attributeExists("article")); //articles date가 있는지

        then(articleService).should().getArticle(articleId);
    }


    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView()
        throws Exception {
        //test


        //When Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/search"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView()
        throws Exception {
        //test


        //When Then
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.of(
            createUserAccountDto(),
            "title",
            "content",
            Set.of(HashtagDto.of("java"))
        );
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
            1L,
            createUserAccountDto(),
            Set.of(),
            "title",
            "content",
            Set.of(HashtagDto.of("java")),
            LocalDateTime.now(),
            "jh",
            LocalDateTime.now(),
            "jh"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            "jh",
            "pw",
            "jh@mail.com",
            "jh",
            "memo",
            LocalDateTime.now(),
            "jh",
            LocalDateTime.now(),
            "jh"
        );
    }

}