package com.fyo.ggboo.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Default Category
 * 
 * @author boolancpain
 */
@Getter
@AllArgsConstructor
public enum DefaultCategory {
    FOOD("식비", 1),
    CAFE("카페", 2),
    SHOPPING("쇼핑", 3),
    TRANSPORTATION("교통", 4),
    PHONE("통신", 5),
    ETC("기타", 6);

    private String alias;

    private int sequence;
}