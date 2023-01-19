package com.fyo.ggboo.domain.account;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;

/**
 * 카테고리 관련 Request Dto
 * 
 * @author boolancpain
 */
public class CategoryRequestDto {
    /**
     * Category Create Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class CategoryCreateDto {
        @Size(min = 2, max = 10, message = "{validation.category.alias}")
        private String alias;
    }

    /**
     * Category Reorder Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class CategoryReorderDto {
        @NotEmpty(message= "{validation.required}")
        private List<Long> categoryIds;
    }
}