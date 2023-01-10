package com.fyo.ggboo.domain.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import com.fyo.ggboo.infra.validator.DateConstraint;

import lombok.Getter;

/**
 * 거래내역 관련 Request Dto
 * 
 * @author boolancpain
 */
public class TransactionRequestDto {
	
	/**
	 * Transaction Create Dto
	 * 
	 * @author boolancpain
	 */
	@Getter
	static class TransactionCreateDto {
		
		@NotNull(message = "{validation.required}")
		@Digits(integer = 10, fraction = 0, message = "{validation.id}")
		private Long categoryId;
		
		@Positive(message = "{validation.transaction.cost.positive}")
		@Max(value = 999999999, message = "{validation.transaction.cost.max}")
		private int cost;
		
		@Length(max = 255, message = "{validation.transaction.remark}")
		private String remark;
		
		@DateConstraint(message = "{validation.date}")
		private String transactedDate;
	}
}