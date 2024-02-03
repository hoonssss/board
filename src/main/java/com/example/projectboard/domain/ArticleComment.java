package com.example.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(indexes = {
    @Index(columnList = "content"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@ToString
@EntityListeners(AuditingEntityListener.class)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //optional = false -> 필수 값
    @Setter @ManyToOne(optional = false) @JoinColumn private Article article; //article(ID)
    @Setter @Column(nullable = false, length = 500) private String content; //내용

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;
    @CreatedBy @Column(nullable = false) private String createdBy;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;
    @LastModifiedBy @Column(nullable = false) private String modifiedBy;

    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleComment articleComment)) {
            return false;
        }
        return id != null && id.equals(articleComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
