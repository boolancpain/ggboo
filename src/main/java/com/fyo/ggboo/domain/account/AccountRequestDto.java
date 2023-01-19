package com.fyo.ggboo.domain.account;

import com.fyo.ggboo.infra.validator.DateConstraint;

import lombok.Getter;

/**
 * 장부 관련 Request Dto
 * 
 * @author boolancpain
 */
public class AccountRequestDto {
    /**
     * Account Search Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class AccountDto {
        private Long accountId;
    }

    /**
     * Transaction Search Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class AccountTransactionDto {
        @DateConstraint(message = "{validation.date}")
        private String date;
    }
}