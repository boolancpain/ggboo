package com.fyo.ggboo.domain.account;

import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 관련 Response Dto
 * 
 * @author boolancpain
 */
public class CategoryResponseDto {
    /**
     * Category Info Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class CategoryInfoDto {
        private Long categoryId;

        private String alias;

        private int sequence;

        @Builder
        public CategoryInfoDto(Long categoryId, String alias, int sequence) {
            this.categoryId = categoryId;
            this.alias = alias;
            this.sequence = sequence;
        }
    }
}