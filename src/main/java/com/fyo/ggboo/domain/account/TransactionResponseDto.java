package com.fyo.ggboo.domain.account;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

/**
 * 거래내역 관련 Response Dto
 * 
 * @author boolancpain
 */
public class TransactionResponseDto {
    /**
     * Transaction Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class TransactionInfoDto {
        private Long transactionId;

        private Long memberId;

        private Long categoryId;

        private int cost;

        private String remark;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime transactedDate;

        @Builder
        public TransactionInfoDto(Long transactionId, Long memberId, Long categoryId, int cost, String remark, LocalDateTime transactedDate) {
            this.transactionId = transactionId;
            this.memberId = memberId;
            this.categoryId = categoryId;
            this.cost = cost;
            this.remark = remark;
            this.transactedDate = transactedDate;
        }
    }
}