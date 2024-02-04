package com.example.projectboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Data Rest Test")
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("api 게시글 리스트 조회")
    @Test
    void test1() throws Exception {
        //Given

        //When & Then
        mvc.perform(MockMvcRequestBuilders.get("/api/articles"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
            .andDo(MockMvcResultHandlers.print());
    }

//    @DisplayName("api 게시글 단건 조회")
//    @Test
//    void test2() throws Exception {
//        //Given
//
//        //When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
//            .andDo(MockMvcResultHandlers.print());
//    }

//    @DisplayName("api 게시글 댓글 조회")
//    @Test
//    void test3() throws Exception {
//        //Given
//
//        //When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/api/articles/1/articleComments"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
//            .andDo(MockMvcResultHandlers.print());
//    }
//
//    @DisplayName("api 댓글 전체 조회")
//    @Test
//    void test4() throws Exception {
//        //Given
//
//        //When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
//            .andDo(MockMvcResultHandlers.print());
//    }
//
//    @DisplayName("api 댓글 단건 조회")
//    @Test
//    void test5() throws Exception {
//        //Given
//
//        //When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments/1"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
//            .andDo(MockMvcResultHandlers.print());
//    }
}
