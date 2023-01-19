package com.fyo.ggboo.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fyo.ggboo.global.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Category Class
 * 
 * @author boolancpain
 */
@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountId;

    @Column(length = 10)
    private String alias;

    @Column(nullable = false)
    private int sequence;

    @Builder
    public Category(Long accountId, String alias, int sequence) {
        this.accountId = accountId;
        this.alias = alias;
        this.sequence = sequence;
    }

    /**
     * 카테고리 명 업데이트
     * 
     * @param description
     */
    public void updateAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 정렬순서 업데이트
     * 
     * @param sequence
     */
    public void updateSequence(int sequence) {
        this.sequence = sequence;
    }
}