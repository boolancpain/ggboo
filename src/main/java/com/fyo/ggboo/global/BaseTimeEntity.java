package com.fyo.ggboo.global;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * Base Time Entity Class
 * 
 * @author boolancpain
 */
@Getter
@MappedSuperclass	// 엔티티 클래스에서 상속받는 경우 BaseTime 클래스의 필드도 컬럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class)	// 데이터의 생성 및 수정 시간을 관리합니다.
public abstract class BaseTimeEntity {
	
	@CreatedDate
	@Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP", updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(columnDefinition="DATETIME(0)")
	private LocalDateTime modifiedDate;
}