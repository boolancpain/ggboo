package com.fyo.ggboo.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 유효성 검사 오류 객체
 * 
 * @author boolancpain
 */
@Getter
@Setter
public class ValidationResponse {
    private String field;

    private String message;

    @Builder
    public ValidationResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }
}