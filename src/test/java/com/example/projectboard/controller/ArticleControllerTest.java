package com.example.projectboard.controller;

import com.example.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("view 컨트롤러")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(MockMvcResultMatchers.model().attributeExists("articles")); //articles date가 있는지
    }

    @DisplayName("[view][GET] 게시글 단일 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(MockMvcResultMatchers.model().attributeExists("article")); //articles date가 있는지
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView()
        throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/search"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 (게시판) 페이지 -> 정상 호출 확인")
    @Test
    void givenNothing_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView()
        throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}