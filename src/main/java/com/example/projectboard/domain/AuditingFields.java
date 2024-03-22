package com.example.projectboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
/**
 * 추상클래스이고 각 필드는 상속 받는 자식 Entity에서 접근 및 수정이 가능해야 함
 * 따라서 private -> protected 로 변경
 **/
public abstract class AuditingFields {

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt; //생성일시

    @CreatedBy
    @Column(length = 100, updatable = false)
    protected String createdBy; //생성자

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @LastModifiedDate
    @Column
    protected LocalDateTime modifiedAt; //수정일시

    @LastModifiedBy
    @Column(length = 100)
    protected String modifiedBy; //수정

}
