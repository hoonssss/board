package com.example.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {

    private Long id;
    private Article article; //article(ID)
    private String content; //내용

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

}
