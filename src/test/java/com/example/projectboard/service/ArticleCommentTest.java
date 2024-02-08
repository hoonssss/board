//package com.example.projectboard.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//import com.example.projectboard.domain.Article;
//import com.example.projectboard.dto.ArticleCommentDto;
//import com.example.projectboard.repository.ArticleCommentRepository;
//import com.example.projectboard.repository.ArticleRepository;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@DisplayName("비즈니스 로직 - 댓글")
//@ExtendWith(MockitoExtension.class)
//public class ArticleCommentTest {
//
//    @InjectMocks
//    private ArticleCommentService sut;
//
//    @Mock
//    private ArticleCommentRepository articleCommentRepository;
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @DisplayName("게시글 ID로 조회하면 해당하는 댓글 리스트 반환")
//    @Test
//    void givenArticleId_whenSearchComments_thenReturnsComments() {
//        //Given
//        Long articleId = 1L;
//
//        given(articleRepository.findById(articleId)).willReturn(
//            Optional.of(Article.of("title","content","hashtag"))
//        );
//
//        //When
//        List<ArticleCommentDto> list = sut.searchArticleComment(articleId);
//
//        //Then
//        assertThat(list).isNotNull();
//        then(articleRepository).should().findById(articleId);
//    }
//}
